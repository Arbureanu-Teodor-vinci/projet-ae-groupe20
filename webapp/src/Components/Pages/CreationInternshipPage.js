import { getAuthenticatedUser } from "../../utils/auths"
import { clearPage } from "../../utils/render";
import Navigate from "../Router/Navigate";

const creationStagePage = async () => {
    if (!getAuthenticatedUser()) {
        Navigate('/login');
        return;
    };

    clearPage();
    await renderCreationStagePage();
};

async function renderCreationStagePage() {
    if (getAuthenticatedUser().role !== 'Etudiant'){
        Navigate('/');
        return;
    }
    const main = document.querySelector('main');
    const urlParams = new URLSearchParams(window.location.search);
    const contactIdParam = urlParams.get('contactId');
    // eslint-disable-next-line no-console
    console.log(contactIdParam);
    const options = {
        method: 'GET',
        headers : {
            'Content-Type': 'application/json',
            "Authorization": `${getAuthenticatedUser().token}`,
        },
    };
    const response = await fetch(`/api/contacts/getOne:${contactIdParam}`, options);
    const contact = await response.json();
    
    const responseEnterprise = await fetch(`/api/enterprises/getOne:${contact.enterpriseId}`, options);
    const enterprise = await responseEnterprise.json();
    
    const responseSupervisor = await fetch(`/api/supervisors/getByEnterprise:${contact.enterpriseId}`, options);
    const supervisors = await responseSupervisor.json();

    // eslint-disable-next-line no-console
    console.log(supervisors);

    main.innerHTML = `
        <section>
            <div style="margin-top: 100px" class="container h-100">
                <div class="row d-flex justify-content-center align-items-center h-100">
                    <div class="col-12 text-center">
                        <h1>Création de mon stage</h1>
                        <form>
                            <div class="form-group">
                                <label for="companyName">Nom commercial</label>
                                <p id="companyName">${enterprise.tradeName}</p>
                            </div>
                            <div class="form-group">
                                <label for="supervisor">Responsable de stage</label>
                                <select id="supervisor" class="form-control">
                                    <option>Choisissez un responsable de stage</option>
                                    ${supervisors.map((supervisor) => `<option value="${supervisor.id}">${supervisor.firstName} ${supervisor.lastName}</option>`).join('')}
                                </select>
                                <div class="d-flex justify-content-center">
                                    <a id="${enterprise.id}" href="#" class="text-primary add-supervisor">Responsable de stage non présent? Ajouter le</a>
                                </div>
                            </div>
                            <div class="form-group">
                              <label for="subject">Sujet du stage</label>
                                <input type="text" class="form-control" id="subject">
                            </div>
                            <div class="form-group">
                                <label for="signatureDate">Date de signature</label>
                                <input type="date" class="form-control" id="signatureDate">
                            </div>
                            <button type="submit" class="btn btn-primary mt-3" id="create">Créer</button>
                            <p class = "errorMessage"><p>
                        </form>
                    </div>
                </div>
            </div>
        </section>`;

    const addSupervisor = document.querySelector('.add-supervisor');
    addSupervisor.addEventListener('click', (e) => {
        e.preventDefault();
        localStorage.setItem('previousPage', window.location.href);
        Navigate(`/creationSupervisor?enterpriseId=${e.target.id}`);
    });

    const form = document.querySelector('form');
    form.addEventListener('submit', async (event) => {
        const supervisorParam = document.getElementById('supervisor').value;
        const subjectParam = document.getElementById('subject').value;
        // eslint-disable-next-line no-console
        console.log(subjectParam);
        const signatureDateParam = document.getElementById('signatureDate').value;
        event.preventDefault();
        
        const optionsCreateInternship = {
            method: 'POST',
            headers : {
                'Content-Type': 'application/json',
                "Authorization": `${getAuthenticatedUser().token}`,
            },
            body: JSON.stringify({
                subject: subjectParam,
                signatureDate: signatureDateParam,
                supervisorId: supervisorParam,
                contactId: contactIdParam,
                academicYear: 1
            }),
        };
        const responseCreateInternship = await fetch(`/api/internships/add`, optionsCreateInternship);
        if(responseCreateInternship.ok){
            event.preventDefault();
            Navigate('/profil');
        }
        else{
            const errorMessage = document.querySelector('.errorMessage');
            errorMessage.innerHTML = 'Erreur lors de la création du stage';
        }
    

    });
}

export default creationStagePage;

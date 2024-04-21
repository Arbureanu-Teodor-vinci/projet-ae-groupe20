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
    const contactId = urlParams.get('contactId');
    // eslint-disable-next-line no-console
    console.log(contactId);
    const options = {
        method: 'GET',
        headers : {
            'Content-Type': 'application/json',
            "Authorization": `${getAuthenticatedUser().token}`,
        },
    };
    const response = await fetch(`/api/contacts/getOne:${contactId}`, options);
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
                                <select id="supervisor" class="form-control" required>
                                    <option value="">Choisissez un responsable de stage</option>
                                    ${supervisors.map((supervisor) => `<option value="${supervisor.id}">${supervisor.firstName} ${supervisor.lastName}</option>`).join('')}
                                </select>
                                <div class="d-flex justify-content-center">
                                    <a id="add-supervisor" href="#" class="text-primary">Responsable de stage non présent? Ajouter le</a>
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
                        </form>
                    </div>
                </div>
            </div>
        </section>`;

    const addSupervisor = document.getElementById('add-supervisor');
    addSupervisor.addEventListener('click', () => {
        Navigate('/creationSupervisor');
    });
}

export default creationStagePage;

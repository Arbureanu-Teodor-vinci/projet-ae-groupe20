/* eslint-disable no-console */
import { getAuthenticatedUser } from "../../utils/auths"
import { clearPage } from "../../utils/render";
import Navigate from "../Router/Navigate";

const creationSupervisorPage = async () => {
    if (!getAuthenticatedUser()) {
        Navigate('/login');
        return;
    };

    clearPage();
    await renderCreationSupervisorPage();
};

async function renderCreationSupervisorPage() {
    if (getAuthenticatedUser().role !== 'Etudiant'){
        Navigate('/');
        return;
    }
    const main = document.querySelector('main');
    const urlParams = new URLSearchParams(window.location.search);
    const enterpriseIdParam = urlParams.get('enterpriseId');
    const options = {
        method: 'GET',
        headers : {
            'Content-Type': 'application/json',
            "Authorization": `${getAuthenticatedUser().token}`,
        },
    };
    const responseEnterprise = await fetch(`/api/enterprises/getOne:${enterpriseIdParam}`, options);
    const enterprise = await responseEnterprise.json();
    console.log(enterprise);
    main.innerHTML = `
        <section>
            <div style="margin-top: 100px" class="container h-100">
                <div class="row d-flex justify-content-center align-items-center h-100">
                    <div class="col-12 text-center">
                        <h1>Création d'un responsable de stage pour ${enterprise}</h1>
                        <form>
                            <div class="form-group">
                                <label for="companyName">Nom commercial</label>
                                <p id="companyName">${enterprise.tradeName}</p>
                            </div>
                            <div class="form-group">
                              <label for="lastName">Nom de famille</label>
                                <input type="text" class="form-control" id="lastName" required>
                            </div>
                            <div class="form-group">
                                <label for="firstName">Prénom</label>
                                <input type="text" class="form-control" id="firstName" required>
                            </div>
                            <div class="form-group">
                                <label for="email">Email</label>
                                <input type="email" class="form-control" id="email" required>
                            </div>
                            <div class="form-group">
                                <label for="phone">Téléphone</label>
                                <input type="tel" class="form-control" id="phone" required>
                            </div>
                            <button type="submit" class="btn btn-primary mt-3" id="create">Créer</button>
                            <p class = "errorMessage"><p>
                        </form>
                    </div>
                </div>
            </div>
        </section>`;

    const form = document.querySelector('form');
    form.addEventListener('submit', async (e) => {
        e.preventDefault();
        const lastNameParam = document.querySelector('#lastName').value;
        const firstNameParam = document.querySelector('#firstName').value;
        const emailParam = document.querySelector('#email').value;
        const phoneParam = document.querySelector('#phone').value;
        const optionsCreateSupervisor = {
            method: 'POST',
            headers : {
                'Content-Type': 'application/json',
                'Authorization': `${getAuthenticatedUser().token}`
            },
            body: JSON.stringify({
                lastName: lastNameParam,
                firstName: firstNameParam,  
                email: emailParam,
                enterpriseId: enterpriseIdParam,
                phoneNumber: phoneParam
            }),                
};

        const responseCreateSupervisor = await fetch('/api/supervisors/add', optionsCreateSupervisor);
        if(responseCreateSupervisor.ok){
            e.preventDefault();
            Navigate('/profil');
        }
        else{
            const errorMessage = document.querySelector('.errorMessage');
            errorMessage.innerHTML = "Erreur lors de la création du responsable de stage";
        }
    });
}

export default creationSupervisorPage;
/* eslint-disable no-alert */
/* eslint-disable no-console */
import { getAuthenticatedUser } from "../../utils/auths"
import { clearPage } from "../../utils/render";
import Navigate from "../Router/Navigate";

const main = document.querySelector('main');

const creationSupervisorPage = async () => {
    if (!getAuthenticatedUser()) {
        main.innerHTML = `
        <section>
            <div style="margin-top: 100px" class="container h-100">
                <div class="row d-flex justify-content-center align-items-center h-100">
                    <div class="col-12 text-center">
                        <h1>Erreur 404</h1>
                        <p>Page non trouvée</p>
                    </div>
                </div>
            </div>
        </section>`;
    }else{
        clearPage();
        await renderCreationSupervisorPage();    
    }
   };

async function renderCreationSupervisorPage() {
    const urlParams = new URLSearchParams(window.location.search);
    const enterpriseIdParam = urlParams.get('enterpriseId');
    if (getAuthenticatedUser().role !== 'Etudiant' || !enterpriseIdParam) {
        Navigate('/');
        return;
    }
    const options = {
        method: 'GET',
        headers : {
            'Content-Type': 'application/json',
            "Authorization": `${getAuthenticatedUser().token}`,
        },
    };
    const responseEnterprise = await fetch(`/api/enterprises/getOne:${enterpriseIdParam}`, options);
    const enterpriseJson = await responseEnterprise.json();
    main.innerHTML = `
        <section>
            <div style="margin-top: 100px" class="container h-100">
                <div class="row d-flex justify-content-center align-items-center h-100">
                    <div class="col-12 text-center">
                        <h1>Création d'un responsable de stage pour ${enterpriseJson.tradeName} ${enterpriseJson.designation != null ? enterpriseJson.designation : ''}</h1>
                        <form>
                            <div class="form-group">
                                <label for="companyName">Nom commercial</label>
                                <p id="companyName">${enterpriseJson.tradeName}</p>
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
                enterprise: enterpriseJson,
                phoneNumber: phoneParam
            }),                
};

        const responseCreateSupervisor = await fetch('/api/supervisors/add', optionsCreateSupervisor);
        
        if(responseCreateSupervisor.ok){
            e.preventDefault();
            const previousPage = localStorage.getItem('previousPage');
            Navigate(previousPage);
        }
        else{
            alert(`${responseCreateSupervisor.status} : ${responseCreateSupervisor.statusText}`);
        }
    });
}

export default creationSupervisorPage;
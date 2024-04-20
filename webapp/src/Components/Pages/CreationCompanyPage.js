/* eslint-disable no-plusplus */
/* eslint-disable no-console */
import { getAuthenticatedUser } from "../../utils/auths"
import { clearPage } from "../../utils/render";
import Navigate from "../Router/Navigate";

const creationCompanyPage = async () => {
    if(!getAuthenticatedUser()){
        Navigate('/login');
        return;
    };

    clearPage();
    await renderCreationCompanyPage();
};

async function renderCreationCompanyPage(){
    const options = {
        method: 'GET',
        headers : {
          'Content-Type': 'application/json',
          'Authorization': `${getAuthenticatedUser().token}`
        },
      };
      const response = await fetch(`/api/enterprises/getAll`, options);
      const companies = await response.json();

    const main = document.querySelector('main');
    main.innerHTML = `
    <section>
        <div class="container h-100">
            <div class="row d-flex justify-content-center align-items-center h-100">
                <div class="col-12 text-center">
                    <h1>Créer une entreprise</h1>
                    <form>
                        <div class="form-group">
                            <label for="companyName">Nom commercial</label>
                            <input type="text" class="form-control" id="companyName" required>
                            <small id="appelationIndicator" class="form-text text-danger" style="display: none;">Indiquez une appellation</small>
                        </div>
                        <div class="form-group">
                            <label for="companyAppellation">Appellation *</label>
                            <input type="text" class="form-control" id="companyAppellation" required>
                            <small id="appelationObligatoire" class="form-text text-danger" style="display: none;">Appellation est obligatoire</small>
                            <small id="appelationOptionnal" class="form-text text-muted">L'appellation n'est obligatoire que si le nom de l'entreprise est déjà connu dans le système.</small>
                            <small id="appelationPresent" class="form-text text-danger" style="display: none;">L'appellation est déjà utilisée.</small>
                        </div>
                        <div class="form-group">
                            <labem for="comapanyCity">Ville</label>
                            <input type="text" class="form-control" id="companyCity" required>
                        </div>
                        <div class="form-group">
                            <label for="companyAddress">Adresse</label>
                            <input type="text" class="form-control" id="companyAdress" required>
                        </div>
                        <div class="form-group">
                            <label for="companyEmail">Email</label>
                            <input type="email" class="form-control" id="companyEmail" required>
                        </div>
                        <div class="form-group">
                            <label for="companyTel">Tel</label>
                            <input type="tel" class="form-control" id="companyTel" required>
                        </div>
                        <button type="submit" class="btn btn-success float-right mt-3" id="create">Ajouter</button>
                        <p class = "errorMessage"><p>
                    </form>
                </div>
            </div>
        </div>
    </section>`;

    const companyName = document.getElementById('companyName');
    const designation = document.getElementById('companyAppellation');
    const appelationIndicator = document.getElementById('appelationIndicator');
    const appelationObligatoire = document.getElementById('appelationObligatoire');
    const appelationOptionnal = document.getElementById('appelationOptionnal');
    const appelationPresent = document.getElementById('appelationPresent');

    appelationIndicator.style.display = 'none';
    appelationObligatoire.style.display = 'none';
    appelationOptionnal.style.display = 'block';
    appelationPresent.style.display = 'none';

    designation.required = false;

    // Check if the company name is already in the database
    companyName.addEventListener('input', () => {
    for (let i = 0; i < companies.length; i++) {
            const tradeNameData = companies[i].tradeName.toLowerCase().replace(/\s/g, '');
            if(companyName.value.toLowerCase().replace(/\s/g, '') === tradeNameData){
                appelationIndicator.style.display = 'block';
                appelationObligatoire.style.display = 'block';
                appelationOptionnal.style.display = 'none';
                designation.required = true;
                break;
            }
            else{
                appelationIndicator.style.display = 'none';
                appelationObligatoire.style.display = 'none';
                appelationOptionnal.style.display = 'block';
                designation.required = false;
            }
        }

        if(designation.required === true){
            designation.addEventListener('input', () => {
                for (let i = 0; i < companies.length; i++) {
                    if(companies[i].designation){

                    if(companies[i].tradeName.toLowerCase().replace(/\s/g, '') === companyName.value.toLowerCase().replace(/\s/g, '') 
                    && companies[i].designation.toLowerCase().replace(/\s/g, '') === designation.value.toLowerCase().replace(/\s/g, '')){
                        appelationPresent.style.display = 'block';
                        appelationObligatoire.style.display = 'none';
                        break;
                    }
                    else{
                        appelationObligatoire.style.display = 'block';
                        appelationPresent.style.display = 'none';
                    }
                }
            }
            })
        }
    });

    

    const form = document.querySelector('form');
    form.addEventListener('submit', async (event) => {
        const companyParam = companyName.value; 
        const designationParam = designation.value;
        const cityParam = document.getElementById('companyCity').value;
        const adresseParam = document.getElementById('companyAdress').value;
        const emailParam = document.getElementById('companyEmail').value;
        const phoneNumberParam = document.getElementById('companyTel').value;

        event.preventDefault();
        const optionsCreateCompany = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `${getAuthenticatedUser().token}`,
            },
            body: JSON.stringify({
                tradeName: companyParam,
                designation : designationParam,
                adresse : adresseParam,
                phoneNumber : phoneNumberParam,
                city : cityParam,
                email : emailParam
            }),
            
        };
        
        const responseCreateCompany = await fetch(`/api/enterprises/add`, optionsCreateCompany);
        if(responseCreateCompany.ok){
            event.preventDefault();
            Navigate('/creationContact');
        }
        else{
            const erreur = document.querySelector('.errorMessage');
            erreur.innerText = "Un des champs est manquant ou incorrect";
        }
    });

};

export default creationCompanyPage;
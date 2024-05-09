import { getAuthenticatedUser } from "../../utils/auths"
import { clearPage } from "../../utils/render";
import Navigate from "../Router/Navigate";

const creationContactPage = async () => {
    if (!getAuthenticatedUser()) {
        Navigate('/login');
        return;
    };

    clearPage();
    await renderCreationContactPage();
};

async function renderCreationContactPage() {
    
    const options = {
        method: 'GET',
        headers : {
          'Content-Type': 'application/json',
          'Authorization': `${getAuthenticatedUser().token}`
        },
      };
      const response = await fetch('/api/enterprises/getAll', options);
      const companies = await response.json();

    const main = document.querySelector('main');

    main.innerHTML = `
    <section>
        <div style="margin-top: 100px" class="container h-100">
            <div class="row d-flex justify-content-center align-items-center h-100">
                <div class="col-12 text-center">
                    <h1>Créer un contact</h1>
                    <form id="contact-form" class="w-100">
                        <div class="form-group row mb-3">
                            <label for="company" class="col-sm-2 col-form-label">Entreprise</label>
                            <div class="col-sm-10">
                                <input type="text" id="company-filter" class="form-control" placeholder="Rechercher une entreprise">
                                <select id="company" name="company" class="form-control" required></select>
                                <div class="d-flex justify-content-center">
                                    <a id="add-company" href="#" class="text-primary">Entreprise non présente? Ajoutez la</a>
                                </div>
                            </div>
                        </div>
                        <button type="submit" class="btn btn-primary" id="create">Créer</button>
                        <p class = "contactExist errorMessage"><p>
                    </form>
                </div>
            </div>
        </div>
    </section>`;


    const addCompanyLink = document.querySelector('#add-company');
    addCompanyLink.addEventListener('click', (event) => {
        event.preventDefault();
        Navigate('/creationCompany');
    });

    const companySelect = document.querySelector('#company');
    const companyFilterInput = document.querySelector('#company-filter');

    // Filtrer les options lors de la saisie dans le champ de filtre
    if (companyFilterInput){
        renderCompaniesOptions(companies);
        companyFilterInput.addEventListener('input', (event) => {
        const filterValue = event.target.value.toLowerCase();
        const filteredCompanies = companies.filter(company => company.tradeName.toLowerCase().startsWith(filterValue));
        renderCompaniesOptions(filteredCompanies);
        });
    }

    // Fonction qui permet de générer les options du select
    function renderCompaniesOptions(companiesToRender) {
        // Réinitialiser les options du select
        companySelect.innerHTML = '';

        // Trier les entreprises par nom
        companiesToRender.sort((a, b) => a.tradeName.localeCompare(b.tradeName));

        if(companiesToRender.length === 0) {
            // Si aucune entreprise n'a été trouvée, ajoutez une option avec le message d'erreur
            const option = document.createElement('option');
            option.text = "Aucun résultat n'a été trouvé.";
            option.value = '';
            companySelect.appendChild(option);
        } else {

        // Ajouter les options filtrées ou toutes les options
        companiesToRender.forEach(company => {
            if(company.blackListed === false){
            const option = document.createElement('option');
            option.text = company.designation ? `${company.tradeName  } - ${  company.designation}` : company.tradeName;
            option.setAttribute('data-company', JSON.stringify(company));
            companySelect.appendChild(option);
            }
            });
        }
    }

    const form = document.querySelector('form');
    form.addEventListener('submit', async (event) => {
        const valueCompany = companySelect.value;
        const { token, ...student } = getAuthenticatedUser();
        console.log(valueCompany);
        if(valueCompany === ''){
            event.preventDefault();
            Navigate('/creationCompany');
        } else {
        event.preventDefault();
        const optionsCreateContact = {
            method: 'POST',
            body: JSON.stringify({
                student,
                enterprise: JSON.parse(companySelect.options[companySelect.selectedIndex].getAttribute('data-company')),
            }),
            headers: {
                'Content-Type': 'application/json',
                "Authorization": `${token}`,
            },
        };
          
          const responseCreateContact = await fetch(`/api/contacts/add`, optionsCreateContact);
          if(responseCreateContact.ok){
            event.preventDefault();
            Navigate('/profil');
          } else {
            alert(`${responseCreateContact.status} : ${responseCreateContact.statusText}`);
          }
        }
    });

    // Initialiser les options avec toutes les entreprises
    renderCompaniesOptions(companies);

    /* function renderCompaniesOptions(companiesToRender) {
        // Réinitialiser les options du select
        companySelect.innerHTML = '';

        // Ajouter les options filtrées ou toutes les options
        companiesToRender.forEach(company => {
            if(company.blackListed === false){
            const option = document.createElement('option');
            option.text = company.tradeName;
            option.setAttribute('data-company', JSON.stringify(company));
            companySelect.appendChild(option);
            }
        });
    } */
}

export default creationContactPage;

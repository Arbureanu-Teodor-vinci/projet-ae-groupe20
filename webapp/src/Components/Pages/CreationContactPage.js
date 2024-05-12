/* eslint-disable no-alert */
import { getAuthenticatedUser } from "../../utils/auths"
import { clearPage } from "../../utils/render";
import Navigate from "../Router/Navigate";

const main = document.querySelector('main');

const creationContactPage = async () => {
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
    await renderCreationContactPage();
    }
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
                        <p class = "errorMessage"><p>
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

    // Filter the options when typing in the filter field
    if (companyFilterInput){
        renderCompaniesOptions(companies);
        companyFilterInput.addEventListener('input', (event) => {
        const filterValue = event.target.value.toLowerCase();
        const filteredCompanies = companies.filter(company => company.tradeName.toLowerCase().startsWith(filterValue));
        renderCompaniesOptions(filteredCompanies);
        });
    }

    // Function that allows generating the select options
    function renderCompaniesOptions(companiesToRender) {
        // Reset the select options
        companySelect.innerHTML = '';

        // Sort companies by name
        companiesToRender.sort((a, b) => a.tradeName.localeCompare(b.tradeName));

        if(companiesToRender.length === 0) {
            // If no company was found, add an option with the error message
            const option = document.createElement('option');
            option.text = "Aucun résultat n'a été trouvé.";
            option.value = '';
            companySelect.appendChild(option);
        } else {

        // Add the filtered options or all options
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

    // Initialize the options with all companies
    renderCompaniesOptions(companies);
}

export default creationContactPage;

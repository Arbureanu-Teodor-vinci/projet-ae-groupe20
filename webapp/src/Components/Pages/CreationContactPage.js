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

      // eslint-disable-next-line no-console
      console.log(companies[0].tradeName);
    
    const main = document.querySelector('main');

    main.innerHTML = `
    <section>
        <div class="container h-100">
            <div class="row d-flex justify-content-center align-items-center h-100">
                <div class="col-12 text-center">
                    <h1>Créer un contact</h1>
                    <form id="contact-form" class="w-100">
                        <div class="form-group row mb-3">
                            <label for="company" class="col-sm-2 col-form-label">Entreprise</label>
                            <div class="col-sm-10">
                                <input type="text" id="company-filter" class="form-control" placeholder="Rechercher une entreprise" required>
                                <select id="company" name="company" class="form-control" required></select>
                                <div class="d-flex justify-content-center">
                                    <a id="add-company" href="#" class="text-primary">Entreprise non présente? Ajouter la</a>
                                </div>
                            </div>
                        </div>
                        <button type="submit" class="btn btn-primary" id="create">Créer</button>
                    </form>
                </div>
            </div>
        </div>
    </section>`;

    const createButton = document.querySelector('#create');
    createButton.addEventListener('click', async (event) => {
        event.preventDefault();
        Navigate('/profil');
    });

    const addCompanyLink = document.querySelector('#add-company');
    addCompanyLink.addEventListener('click', (event) => {
        event.preventDefault();
        Navigate('/creationCompany');
    });

    const companySelect = document.querySelector('#company');
    const companyFilterInput = document.querySelector('#company-filter');

    // Filtrer les options lors de la saisie dans le champ de filtre
    companyFilterInput.addEventListener('input', (event) => {
        const filterValue = event.target.value.toLowerCase();
        const filteredCompanies = companies.filter(company => company.tradeName.toLowerCase().includes(filterValue));
        renderCompaniesOptions(filteredCompanies);
    });

    // Initialiser les options avec toutes les entreprises
    renderCompaniesOptions(companies);

    function renderCompaniesOptions(companiesToRender) {
        // Réinitialiser les options du select
        companySelect.innerHTML = '';

        // Ajouter les options filtrées ou toutes les options
        companiesToRender.forEach(company => {
            const option = document.createElement('option');
            option.value = company.value;
            option.text = company.tradeName;
            companySelect.appendChild(option);
        });
    }
}

export default creationContactPage;

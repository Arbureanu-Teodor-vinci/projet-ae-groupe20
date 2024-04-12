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
                        </div>
                        <div class="form-group">
                            <label for="companyAppellation">Appellation *</label>
                            <input type="text" class="form-control" id="companyAppellation">
                            <small class="form-text text-muted">L'appellation n'est obligatoire que si le nom de l'entreprise est déjà connu dans le système.</small>
                        </div>
                        <div class="form-group">
                            <label for="companyAddress">Adresse</label>
                            <input type="text" class="form-control" id="companyAddress" required>
                        </div>
                        <div class="form-group">
                            <label for="companyEmail">Email</label>
                            <input type="email" class="form-control" id="companyEmail" required>
                        </div>
                        <div class="form-group">
                            <label for="companyTel">Tel</label>
                            <input type="tel" class="form-control" id="companyTel" required>
                        </div>
                        <button type="submit" class="btn btn-success float-right mt-3">Ajouter</button>
                    </form>
                </div>
            </div>
        </div>
    </section>`;

    // eslint-disable-next-line func-names
    document.getElementById('companyName').addEventListener('input', function() {
        let companyName = this.value;
        companyName = companyName.toLowerCase().replace(/\s/g, '');
        // eslint-disable-next-line no-console
        console.log(companyName);
        
    });
}

export default creationCompanyPage;
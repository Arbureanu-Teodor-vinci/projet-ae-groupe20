import { clearPage } from '../../utils/render';
import Navigate from '../Router/Navigate';

const RegisterPage = async () => {
    clearPage();
    await renderRegisterPage();
}

async function renderRegisterPage() {
    const main = document.querySelector('main');
    main.innerHTML = `
    <div id="privacyPolicyWrapper"></div>
    <section>
        <div class="container h-100">
            <div class="row d-flex justify-content-center align-items-center h-100">
                <div class="col-xl-9 mb-5">
                    <h1 class="text-primary text-decoration-underline mb-4 mt-3">Inscription</h1>
                        <form class="card text-dark  border-primary bg-white mb-3" style="border-radius: 15px;">
                            <div class="card-body">

                                <div class="row align-items-center pt-4 pb-3">
                                    <div class="col-md-3 ps-5">
                                        <h6 class="mb-0">Prenom*</h6>
                                    </div>
                                    <div class="col-md-9 pe-5">
                                        <input type="text" class="form-control form-control-lg" id="firstNameInput" required >
                                    </div>
                                </div>

                                <hr class="mx-n3">

                                <div class="row align-items-center pt-4 pb-3">
                                    <div class="col-md-3 ps-5">
                                        <h6 class="mb-0">Nom*</h6>
                                    </div>
                                    <div class="col-md-9 pe-5">
                                        <input type="text" class="form-control form-control-lg" id="nameInput" required >
                                    </div>
                                </div>

                                <hr class="mx-n3">

                                <div class="row align-items-center pt-4 pb-3">
                                    <div class="col-md-3 ps-5">
                                        <h6 class="mb-0">Adresse e-mail*</h6>
                                    </div>
                                    <div class="col-md-9 pe-5">
                                        <input type="text" class="form-control form-control-lg" id="emailInput" required >
                                    </div>
                                </div>
    
                                <hr class="mx-n3">
    
                                <div class="row align-items-center py-3">
                                    <div class="col-md-3 ps-5">
                                        <h6 class="mb-0">Mot de passe*</h6>
                                    </div>
                                    <div class="col-md-9 pe-5">
                                        <input type="password" class="form-control form-control-lg" id="passwordInput" required >
                                    </div>
                                </div>

                                <hr class="mx-n3">

                                <div class="row align-items-center py-3">
                                    <div class="col-md-3 ps-5">
                                        <h6 class="mb-0">Numéro de téléphone*</h6>
                                    </div>
                                    <div class="col-md-9 pe-5">
                                        <input type="password" class="form-control form-control-lg" id="phoneNumberInput" required >
                                    </div>
                                </div>

                                <hr class="mx-n3">

                                <div class="mx-5">
                                    <a href="" id="toLogin"> Déjà un compte ? Connectez-vous ici</a>
                                </div>
   
                            </div>

                            <div class="card-body text-dark mx-3">Les champs marqués d'une * sont obligatoires </div>

                            <div class="px-5 py-4">
                                <input type="submit" class="btn btn-primary btn-lg" id="submit" value="Inscription">
                            </div>
                        </form>
                </div>
            </div>
        </div>
    </section>
    `;

    const toConnexionButton = document.querySelector('#toLogin');
    toConnexionButton.addEventListener('click', (e) => {
        e.preventDefault();
        Navigate('/login');
    });
};

export default RegisterPage;
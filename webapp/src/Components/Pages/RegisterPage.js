/* eslint-disable no-alert */
import { clearPage } from '../../utils/render';
import Navigate from '../Router/Navigate';
import { getAuthenticatedUser } from '../../utils/auths';
import Navbar from '../Navbar/Navbar';

const RegisterPage = async () => {
  if (getAuthenticatedUser()) {
    Navigate('/');
    return;
  }
  clearPage();
  await renderRegisterPage();
  const form = document.querySelector('form');
  form.addEventListener('submit', register);
};

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
                                        <input type="text" class="form-control form-control-lg" id="lastNameInput" required >
                                    </div>
                                </div>

                                <hr class="mx-n3">

                                <div class="row align-items-center pt-4 pb-3">
                                    <div class="col-md-3 ps-5">
                                        <h6 class="mb-0">Adresse e-mail*</h6>
                                    </div>
                                    <div class="col-md-6 pe-5">
                                        <input type="text" class="form-control form-control-lg" id="emailInput" required >
                                    </div>
                                    <div class="col-md-3 pe-5">
                                        <select id="emailDomainSelect" class="form-select">
                                            <option selected>@student.vinci.be</option>
                                            <option>@vinci.be</option>
                                        </select>
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

                                <div class="row align-items-center py-3" id="roleRow">
                                    <div class="col-md-3 ps-5">
                                        <h6 class="mb-0">Rôle*</h6>
                                    </div>
                                    <div class="col-md-9 pe-5">
                                        <select id="role" name="choice">
                                            <option value="">Choisir le rôle</option>
                                            <option value="option1">Professeur</option>
                                            <option value="option2">Admin</option>
                                        </select>
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

  // Get the role row and email domain select elements
  const roleRow = document.querySelector('#roleRow');
  const emailDomainSelect = document.querySelector('#emailDomainSelect');

  // Hide the role row if the initial selected email domain is '@student.vinci.be'
  if (emailDomainSelect.value === '@student.vinci.be') {
    roleRow.style.display = 'none';
  }

  document
    .querySelector('#emailDomainSelect')
    .addEventListener('change', function handleDomainChange() {
      const selectedDomain = this.value;

      if (selectedDomain === '@student.vinci.be') {
        roleRow.style.display = 'none'; // hide the role input
      } else if (selectedDomain === '@vinci.be') {
        roleRow.style.display = 'flex'; // show the role input
      }
    });

  const toConnexionButton = document.querySelector('#toLogin');
  toConnexionButton.addEventListener('click', (e) => {
    e.preventDefault();
    Navigate('/login');
  });
}

async function register(e) {
  e.preventDefault();
  const email =
    document.querySelector('#emailInput').value +
    document.querySelector('#emailDomainSelect').value;
  const password = document.querySelector('#passwordInput').value;
  const firstName = document.querySelector('#firstNameInput').value;
  const lastName = document.querySelector('#lastNameInput').value;
  const telephoneNumber = document.querySelector('#phoneNumberInput').value;
  const userRole = document.querySelector('#role').value;

  let role;
  if (userRole === 'option1') {
    role = 'Professeur';
  } else if (userRole === 'option2') {
    role = 'Administratif';
  } else {
    role = 'Etudiant';
  }

  const options = {
    method: 'POST',
    body: JSON.stringify({
      email,
      password,
      role,
      firstName,
      lastName,
      telephoneNumber,
    }),
    headers: {
      'Content-Type': 'application/json',
    },
  };

  const response = await fetch(`/api/auths/register`, options);

  if (!response.ok) {
    const errorMessage = await response.text();
    alert(`${response.status} : ${errorMessage}`);
  } else {
    Navigate('/login');
    Navbar();
  }

  return null;
}

export default RegisterPage;

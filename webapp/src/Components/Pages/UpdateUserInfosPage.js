import { clearPage } from '../../utils/render';
import {
  getAuthenticatedUser,
  clearAuthenticatedUser,
  setAuthenticatedUser,
} from '../../utils/auths';
import Navigate from '../Router/Navigate';

const UpdateUserInfosPage = async () => {
  clearPage();
  await renderUpdateUserInfos();
};

async function renderUpdateUserInfos() {
  const main = document.querySelector('main');
  main.innerHTML = `
    <section>
        <div class="container h-100">
            <div class="row d-flex justify-content-center align-items-center h-100">
                <div class="col-12 text-center">
                    <h1>Modifier mes données personnelles</h1>
                    <form id="update-user-info-form">
                        <div class="form-group">
                            <label for="email">Email</label>
                            <input type="email" id="email" name="email" class="form-control" value="${
                              getAuthenticatedUser().email
                            }" disabled>
                        </div>
                        <div class="form-group">
                            <label for="firstName">Prénom</label>
                            <input type="text" id="firstName" name="firstName" class="form-control" value="${
                              getAuthenticatedUser().firstName
                            }" required>
                        </div>
                        <div class="form-group">
                            <label for="lastName">Nom</label>
                            <input type="text" id="lastName" name="lastName" class="form-control" value="${
                              getAuthenticatedUser().lastName
                            }" required>
                        </div>
                        <div class="form-group">
                            <label for="telephoneNumber">Numéro de téléphone</label>
                            <input type="tel" id="telephoneNumber" name="telephoneNumber" class="form-control" value="${
                              getAuthenticatedUser().telephoneNumber
                            }" required>
                        </div>
                        
                    <div class="passwordForm">
                     <a href="#" id="showUpdatePasswordForm">Modifier mon mot de passe</a><p> </p>
                    </div>
                        <button id="updateProfileSubmit" type="submit" class="btn btn-primary">Mettre à jour</button>
                    </form>
                    <p id="error-message" class="text-danger"></p>
                </div>
            </div>
        </div>
    </section>`;

  const errorMessage = document.getElementById('error-message');
  // const showUpdatePasswordForm = document.getElementById('showUpdatePasswordForm');
  const passwordFormDiv = document.querySelector('.passwordForm');

  let changePassword = false;

  document.addEventListener('click', (e) => {
    if (e.target.id === 'showUpdatePasswordForm') {
      passwordFormDiv.innerHTML = `
      <div class="form-group">
                      <label for="actualPassword">Mot de passe actuel</label>
                      <div class="input-group">
                          <input type="password" id="actualPassword" name="password" class="form-control" required>
                          <button type="button" class="btn btn-secondary">Afficher</button>
                      </div>
                  </div>
                  <div class="form-group">
                      <label for="newPassword">Nouveau mot de passe</label>
                      <div class="input-group">
                          <input type="password" id="newPassword" name="newPassword" class="form-control" required>
                          <button type="button" class="btn btn-secondary">Afficher</button>
                      </div>
                  </div>
                  <div class="form-group">
                      <label for="newPasswordValidation">Valider nouveau mot de passe</label>
                      <div class="input-group">
                          <input type="password" id="newPasswordValidation" name="newPasswordValidation" class="form-control" required>
                          <button type="button" class="btn btn-secondary">Afficher</button>
                      </div>
                  </div>
                  <a href="#" id="unshowUpdatePasswordForm">Ne pas modifier mon mot de passe</a><p> </p>`;
      changePassword = true;
    }
    if (e.target.classList.contains('btn-secondary')) {
      const passwordInput = document.getElementById(e.target.previousElementSibling.id);
      const type = passwordInput.getAttribute('type') === 'password' ? 'text' : 'password';
      passwordInput.setAttribute('type', type);
    }
    if(e.target.id === 'unshowUpdatePasswordForm') {
      passwordFormDiv.innerHTML = `
      <a href="#" id="showUpdatePasswordForm">Modifier mon mot de passe</a><p> </p>`;
      changePassword = false;
    }
  });


  document.getElementById('updateProfileSubmit').addEventListener('click', async (event) => {
    event.preventDefault();

    const newPasswordInput = document.getElementById('newPassword');
    const newPasswordValidationInput = document.getElementById('newPasswordValidation');

    const bodyUpdateProfile = {
      id: getAuthenticatedUser().id,
      firstName: document.getElementById('firstName').value,
      lastName: document.getElementById('lastName').value,
      telephoneNumber: document.getElementById('telephoneNumber').value,
      role: getAuthenticatedUser().role,
      email: getAuthenticatedUser().email,
      registrationDate: getAuthenticatedUser().registrationDate,
      password: null,
      version: getAuthenticatedUser().version,
    };

    const optionsUpdateProfile = {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
        Authorization: `${getAuthenticatedUser().token}`,
      },
      body: JSON.stringify(bodyUpdateProfile),
    };

    if (changePassword) {
      const bodyLogin = {
        email: getAuthenticatedUser().email,
        password: document.getElementById('actualPassword').value,
      };
      const optionsLogin = {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(bodyLogin),
      };
      const responseLogin = await fetch('/api/auths/login', optionsLogin);
      if (responseLogin.ok) {
        if (newPasswordInput.value === newPasswordValidationInput.value) {
          bodyUpdateProfile.password = document.getElementById('newPasswordValidation').value;
          optionsUpdateProfile.body = JSON.stringify(bodyUpdateProfile);
        } else {
          errorMessage.textContent = 'Les mots de passe ne correspondent pas';
          return;
        }
      } else {
        errorMessage.textContent = 'Mot de passe actuel incorrect';
        return;
      }
      const responseUpdateProfile = await fetch(`/api/auths/updateProfile`, optionsUpdateProfile);
      if (responseUpdateProfile.status === 200) {
        clearAuthenticatedUser();
        Navigate('/login');
      }
    } else {
      const responseUpdateProfile = await fetch(`/api/auths/updateProfile`, optionsUpdateProfile);
      if (responseUpdateProfile.status === 200) {
        const userUpdated = await responseUpdateProfile.json();
        userUpdated.token = getAuthenticatedUser().token;
        clearAuthenticatedUser();
        setAuthenticatedUser(userUpdated);
        Navigate('/profil');
      }
    }
  });
}
export default UpdateUserInfosPage;

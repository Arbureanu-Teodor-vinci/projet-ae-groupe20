import { clearPage} from '../../utils/render';
import {  getAuthenticatedUser } from '../../utils/auths'
import Navigate from '../Router/Navigate';

const main = document.querySelector('main');

const AllUsersPage = async () => {
    if(!getAuthenticatedUser() || getAuthenticatedUser().role === 'Etudiant'){
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
        renderPage();
        }
};


async function renderPage() {
    const options = {
        method: 'GET',
        headers : {
          'Content-Type': 'application/json',
          'Authorization': `${getAuthenticatedUser().token}`
        },
      };
      const response = await fetch('/api/auths/users', options);
      const users = await response.json();

    main.innerHTML = `
    <div style="margin-top: 100px" class="container h-100">
        <div class="row">
            <div class="col-md-12">
                <h1 class="text-primary text-decoration-underline mb-4 mt-3">Tout les utilisateurs</h1>
                <p>Voici la liste de tout les utilisateurs :</p>
            <table class="table">
                <thead>
                    <tr>
                        <th>Email</th>
                        <th>Nom</th>
                        <th>Prénom</th>
                        <th>Numéro de téléphone</th>
                        <th>Rôle</th>
                        <th>Date de création</th>
                        <th>Année académique</th>
                    </tr>
                </thead>
                <tbody>
                </tbody>
            </table>
            </div>
        </div>
        `;
    const usersTable = document.querySelector('.table tbody');
    users.forEach(user => {
        usersTable.innerHTML += `
          <tr>
            <td>${user.email}</td>
            <td>${user.firstName}</td>
            <td>${user.lastName}</td>
            <td>${user.telephoneNumber}</td>
            <td>${user.role}</td>
            <td>${user.registrationDate}</td>
            <td>${user.role === 'Etudiant' ? user.academicYear.year : ' - '}</td>
            <td>${user.role === 'Etudiant' ? `<button id="${user.id}" class="btn btn-primary viewInfo">Voir les informations de cet(tte) étudiant(e)</button>` : ''}</td>
        </tr>
        `;
    });

    
    users.forEach(user => {
        const viewInfoButton = document.getElementById(`${user.id}`);
        if(viewInfoButton){
        viewInfoButton.addEventListener('click', () => {
            Navigate(`/studentInfo?userId=${user.id}`);
        });
    }
    });
}


export default AllUsersPage;
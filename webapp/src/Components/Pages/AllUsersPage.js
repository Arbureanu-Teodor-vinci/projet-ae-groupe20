import { clearPage} from '../../utils/render';
import {  getAuthenticatedUser } from '../../utils/auths'
import Navigate from '../Router/Navigate';

const AllUsersPage = () => {
    if(!getAuthenticatedUser()){
        Navigate('/login');
      }else if(getAuthenticatedUser().role === 'Etudiant'){
          Navigate('/');
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


    const main = document.querySelector('main');
    main.innerHTML = `
    <div class="container">
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
            <td>${user.phoneNumber}</td>
            <td>${user.role}</td>
            <td>${user.registrationDate}</td>
        </tr>
        `;
    }
    );

};

export default AllUsersPage;
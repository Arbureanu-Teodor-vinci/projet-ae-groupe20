import { clearPage} from '../../utils/render';
import {  getAuthenticatedUser } from '../../utils/auths'
import Navigate from '../Router/Navigate';

const AllUsersPage = () => {
    if(!getAuthenticatedUser()){
        Navigate('/login');
      }else if(getAuthenticatedUser().role === 'étudiant'){
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
        <h1 class="text-primary text-decoration-underline mb-4 mt-3">Tous les utilisateurs</h1>
        <p>Voici la liste de tous les utilisateurs</p>
        <ul class="list-group"> </ul>
        </div>
    </div>
    `;
    const list = document.querySelector('.list-group');
    users.forEach(user => {
        list.innerHTML += `
        <li class="list-group-item">
            <h5>${user.email}</h5>
            <p>${user.role}</p>
        </li>
        `;
    }
    );

};

export default AllUsersPage;
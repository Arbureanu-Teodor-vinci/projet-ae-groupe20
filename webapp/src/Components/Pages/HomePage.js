import { clearPage} from '../../utils/render';
import {  getAuthenticatedUser, clearAuthenticatedUser } from '../../utils/auths'
import Navigate from '../Router/Navigate';

const HomePage = () => {
  if(!getAuthenticatedUser()){
    Navigate('/login');
  }else{
    clearPage();
    renderHomePage();
  }
    
};

async function renderHomePage() {
  
    const main = document.querySelector('main');
    main.innerHTML = `<div class="container">
    <div class="row">
        <div class="col-md-12">
        <h1 class="text-primary text-decoration-underline mb-4 mt-3">Accueil</h1>
        <p>Bienvenue sur notre site web</p>
        <p class="userAuth"><p>
    </div>
  `;
  const options = {
    method: 'GET',
    headers : {
      'Content-Type': 'application/json',
      'Authorization': `${getAuthenticatedUser().token}`
    },
  };
  const response = await fetch('/api/auths', options);

  if(response.ok){
    const userAuth = document.querySelector('.userAuth');
    userAuth.innerText = `${getAuthenticatedUser().email}`;

    main.innerHTML += ` <button id="logoutButton">Se déconnecter</button>`
    const logoutButton = document.getElementById('logoutButton');
    logoutButton.addEventListener('click', () => {
      clearAuthenticatedUser();
      Navigate('/login');
    });
  }else{
    clearAuthenticatedUser();
    Navigate('/login');
  }
}
export default HomePage;

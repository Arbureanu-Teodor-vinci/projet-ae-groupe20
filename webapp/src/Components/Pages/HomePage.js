import { clearPage} from '../../utils/render';
import { isAuthenticated, getAuthenticatedUser, clearAuthenticatedUser } from '../../utils/auths'
import Navigate from '../Router/Navigate';

const HomePage = () => {
  if(!getAuthenticatedUser()){
    Navigate('/login');
  }else{
    clearPage();
    renderHomePage();
  }
    
};

function renderHomePage() {
  
    const main = document.querySelector('main');
  main.innerHTML = `<div class="container">
    <div class="row">
        <div class="col-md-12">
        <h1 class="text-primary text-decoration-underline mb-4 mt-3">Accueil</h1>
        <p>Bienvenue sur notre site web</p>
        <p class="userAuth"><p>
    </div>
  `;

  if(isAuthenticated()){
    const userAuth = document.querySelector('.userAuth');
    userAuth.innerText = `${getAuthenticatedUser().email}`;

    main.innerHTML += ` <button id="logoutButton">Se déconnecter</button>`
    const logoutButton = document.getElementById('logoutButton');
    logoutButton.addEventListener('click', () => {
      clearAuthenticatedUser();
      Navigate('/login');
    });
  }else{
    Navigate('/login');
  }
}
export default HomePage;

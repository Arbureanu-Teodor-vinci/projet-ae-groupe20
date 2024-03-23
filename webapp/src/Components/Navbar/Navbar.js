import Navigate from '../Router/Navigate';
// eslint-disable-next-line import/extensions
import { clearAuthenticatedUser, getAuthenticatedUser } from '../../utils/auths.js';

const Navbar = () => {
    renderNavbar();

};

function renderNavbar() {
    const navbarWrapper = document.querySelector('#navbarWrapper');
  
  if(!getAuthenticatedUser()){
    navbarWrapper.innerHTML = `
    <nav class="navbar navbar-expand-sm navbar-light" id="UpPage">

      <div class="d-flex justify-content-between w-100 pt-3">
         <div class="nav nav-underline ps-5">
          <a class="nav-link" style="font-size: 20px" href="#" data-uri="/login">Accueil</a> 
          </div>

        <div class="nav nav-underline pe-5">
        <a class="nav-link" style="font-size: 20px" href="#" data-uri="/login">Se connecter</a>
          <a class="nav-link" style="font-size: 20px" href="#" data-uri="/register">S'inscrire</a>
        </div>
      </div>
    </nav>
  `
  
  }else{ 
    if(getAuthenticatedUser().role === 'étudiant'){
    navbarWrapper.innerHTML = `
    <nav class="navbar navbar-expand-sm navbar-light" id="UpPage">

      <div class="d-flex justify-content-between w-100 pt-3">
         <div class="nav nav-underline ps-5">
          <a class="nav-link" style="font-size: 20px" href="#" data-uri="/">Accueil</a> 
          <a class="nav-link" style="font-size: 20px" href="#" data-uri="/profil">Mon profil</a>
          </div>

        <div class="nav nav-underline pe-5">
        <a class="nav-link" style="font-size: 20px" id="logout">
          <button type="button" class="btn btn-primary">Se déconnecter</button>
        </a>
        </div>
      </div>
    </nav>
  `
  }else{
    navbarWrapper.innerHTML = `
    <nav class="navbar navbar-expand-sm navbar-light" id="UpPage">

      <div class="d-flex justify-content-between w-100 pt-3">
         <div class="nav nav-underline ps-5">
          <a class="nav-link" style="font-size: 20px" href="#" data-uri="/">Accueil</a> 
          <a class="nav-link" style="font-size: 20px" href="#" data-uri="/profil">Mon profil</a>
          <a class="nav-link" style="font-size: 20px" href="#" data-uri="/allusers">See all users</a>
          </div>

        <div class="nav nav-underline pe-5">
        <a class="nav-link" style="font-size: 20px" id="logout">
          <button type="button" class="btn btn-primary">Se déconnecter</button>
        </a>
        </div>
      </div>
    </nav>
  `
    };
  
  const logoutButton = document.querySelector('#logout');
    if (logoutButton) {
      logoutButton.addEventListener('click', () => {
        clearAuthenticatedUser();
      Navbar();
      Navigate('/login');
      });
    };
};

}
export default Navbar;

// import Navigate from '../Router/Navigate';
// import { clearAuthenticatedUser, getAuthenticatedUser } from '../../utils/auths.js';

const Navbar = () => {
    renderNavbar();
};

function renderNavbar() {
    const navbarWrapper = document.querySelector('#navbarWrapper');
    navbarWrapper.innerHTML = `
    <nav class="navbar navbar-expand-sm navbar-light" id="UpPage">

      <div class="d-flex justify-content-between w-100 pt-3">
         <div class="nav nav-underline ps-5">
          <a class="nav-link" style="font-size: 20px" href="#" data-uri="/">Accueil</a> 
        </div>

        <div class="nav nav-underline pe-5">
        <a class="nav-link" style="font-size: 20px" href="#" data-uri="/login">Se connecter</a>
          <a class="nav-link" style="font-size: 20px" href="#" data-uri="/register">S'inscrire</a>
        </div>
      </div>
    </nav>
  `
    
};

export default Navbar;
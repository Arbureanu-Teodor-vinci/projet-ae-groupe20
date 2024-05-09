import Navigate from '../Router/Navigate';
// eslint-disable-next-line import/extensions
import { clearAuthenticatedUser, getAuthenticatedUser } from '../../utils/auths.js';
import logoNav from '../../img/logoNav.png';
import dashboardLogo from '../../img/dashboardLogo.png';
import profilLogo from '../../img/profilLogo.png';
import usersLogo from '../../img/usersLogo.png';
import logoutLogo from '../../img/logoutLogo.png';

const Navbar = () => {
    renderNavbar();

};

function renderNavbar() {
    const navbarWrapper = document.querySelector('#navbarWrapper');
  
  if(!getAuthenticatedUser()){
    navbarWrapper.innerHTML = ``
  
  }else{ 
    if(getAuthenticatedUser().role === 'Etudiant'){
    navbarWrapper.innerHTML = `
    <div class="px-3 py-2 bg-dark text-white fixed-top">
      <div class="container">
        <div class="d-flex flex-wrap align-items-center justify-content-center justify-content-lg-start">
          <a href="#" class="d-flex align-items-center my-2 my-lg-0 me-lg-auto text-white text-decoration-none">
            <img src="${logoNav}" width="64" height="64" alt="Bootstrap" data-uri="/" class="logo-img">
          </a>
          <ul class="nav col-12 col-lg-auto my-2 justify-content-center my-md-0 text-small">
            <li>
              <a href="#" class="nav-link text-white">
                <img src="${profilLogo}" class="bi d-block mx-auto mb-1" width="24" height="24" data-uri="/profil">
                Profil
              </a>
            </li>
            <li>
              <a href="#" class="nav-link text-white">
                <img src="${logoutLogo}" id="logout" class="bi d-block mx-auto mb-1" width="24" height="24">
                Se déconnecter
              </a>
            </li>
          </ul>
        </div>
      </div>
    </div>
    <div style="border-top: 1px solid #ccc; margin-top: 20px;"></div>
  `
  }else{
    navbarWrapper.innerHTML = `
    <div class="px-3 py-2 bg-dark text-white fixed-top">
      <div class="container">
        <div class="d-flex flex-wrap align-items-center justify-content-center justify-content-lg-start">
          <a href="#" class="d-flex align-items-center my-2 my-lg-0 me-lg-auto text-white text-decoration-none">
            <img src="${logoNav}" width="64" height="64" alt="Bootstrap" data-uri="/" class="logo-img">
          </a>
          <ul class="nav col-12 col-lg-auto my-2 justify-content-center my-md-0 text-small">
            
            <li>
              <a href="#" class="nav-link text-white">
                <img src="${dashboardLogo}" class="bi d-block mx-auto mb-1" width="24" height="24" data-uri="/board">
                Tableau de bord
              </a>
            </li>
            <li>
              <a href="#" class="nav-link text-white">
                <img src="${profilLogo}" class="bi d-block mx-auto mb-1" width="24" height="24" data-uri="/profil">
                Profil
              </a>
            </li>
            <li>
              <a href="#" class="nav-link text-white">
                <img src="${usersLogo}" class="bi d-block mx-auto mb-1" width="24" height="24" data-uri="/allUsers">
                Tous les utilisateurs
              </a>
            </li>
            <li>
              <a href="#" class="nav-link text-white">
                <img src="${usersLogo}" class="bi d-block mx-auto mb-1" width="24" height="24" data-uri="/allStudents">
                Tous les étudiants
              </a>
            </li>
            <li style="margin-top: 15px;">
              <form class="d-flex" id="searchForm">
                <input class="form-control me-2" type="search" id="searchInput" placeholder="Rechercher" aria-label="Recherche">
                <button class="btn btn-outline-success" type="submit" id="searchSubmit">Rechercher</button>
              </form>
            </li>
            <li>
              <a href="#" class="nav-link text-white">
                <img src="${logoutLogo}" id="logout" class="bi d-block mx-auto mb-1" width="24" height="24">
                Se déconnecter
              </a>
            </li>
          </ul>
        </div>
      </div>
    </div>
  `
    };
    /*
    const searchForm = document.querySelector('#searchForm');
    */
    const searchInput = document.querySelector('#searchInput');
    
    const searchButton = document.querySelector('#searchSubmit');
    if(searchButton){
      searchButton.addEventListener('click', (e) => {
          if(searchInput.value.trim() === ""){
          e.preventDefault();
          Navigate('/allStudents');
          }
          else{
          e.preventDefault();
          Navigate(`/allStudents?search=${searchInput.value}`);
          }
        });
    }
  
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

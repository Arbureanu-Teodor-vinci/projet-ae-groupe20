import { clearPage} from '../../utils/render';
import {  getAuthenticatedUser } from '../../utils/auths'
import Navigate from '../Router/Navigate';

const main = document.querySelector('main');

const AllUsersPage = () => {
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
    let users = await response.json();

    // Fetch all academic years
    const responseAcademicYears = await fetch('/api/academicYear/all', options);
    const academicYears = await responseAcademicYears.json();

    const urlParams = new URLSearchParams(window.location.search);
    const search = urlParams.get('search');
    if(search){
        const filteredUsers = users.filter(user => user.firstName.toLowerCase().includes(search.toLowerCase()) || user.lastName.toLowerCase().includes(search.toLowerCase()));
        users = filteredUsers;
    }
    main.innerHTML = `
    <section>
    <div style="margin-top: 100px" class="container h-100">
        <div class="row">
            <div class="col-md-12">
                <h1 class="text-primary text-decoration-underline mb-4 mt-3">Tout les utilisateurs</h1>
                <select id="academicYearFilter">
                    <option value="">Tous les années</option>
                    ${academicYears.reverse().map(year => `<option value="${year}">${year}</option>`).join('')}
                </select>
                <p>Voici la liste de tout les étudiants :</p>
            <table class="table">
                <thead>
                    <tr>
                        <th>Email</th>
                        <th>Nom</th>
                        <th>Prénom</th>
                        <th>Numéro de téléphone</th>
                        <th>Date de création</th>
                        <th>Année académique</th>
                    </tr>
                </thead>
                <tbody>
                </tbody>
            </table>
            </div>
        </div>
    </div>
    </section>
        `;
    const usersTable = document.querySelector('.table tbody');
    users.forEach(user => {
        if(user.role === 'Etudiant'){
        usersTable.innerHTML += `
          <tr>
            <td>${user.email}</td>
            <td>${user.firstName}</td>
            <td>${user.lastName}</td>
            <td>${user.telephoneNumber}</td>
            <td>${user.registrationDate}</td>
            <td>${user.academicYear.year}</td>
            <td><button id="${user.id}" class="btn btn-primary viewInfo">Voir les informations de cet(tte) étudiant(e)</button></td>
        </tr>
        `;
        }
    });

    const academicYearFilter = document.querySelector('#academicYearFilter');

    academicYearFilter.addEventListener('change', function filterUsersByYear() {
        let filteredUsers = users;
        if (this.value) {
            filteredUsers = users.filter(user => user.academicYear && user.academicYear.year === this.value);
        }
        usersTable.innerHTML = '';
        filteredUsers.forEach(user => {
            if(user.role === 'Etudiant'){
                usersTable.innerHTML += `
                <tr>
                    <td>${user.email}</td>
                    <td>${user.firstName}</td>
                    <td>${user.lastName}</td>
                    <td>${user.telephoneNumber}</td>
                    <td>${user.registrationDate}</td>
                    <td>${user.academicYear.year}</td>
                    <td><button id="${user.id}" class="btn btn-primary viewInfo">Voir les informations de cet(tte) étudiant(e)</button></td>
                </tr>
                `;
            }
        });
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
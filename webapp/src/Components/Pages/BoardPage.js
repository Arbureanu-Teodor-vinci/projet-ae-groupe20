import Chart from 'chart.js/auto';
import { clearPage } from "../../utils/render";
import { getAuthenticatedUser } from '../../utils/auths';
import Navigate from '../Router/Navigate';

const BoardPage = async () => {
    clearPage();
    await renderBoardPage();
}

async function renderBoardPage() {
    const options = {
        method: 'GET',
        headers : {
            'Content-Type': 'application/json',
            "Authorization": `${getAuthenticatedUser().token}`,
        },
    };

    const response = await fetch(`/api/enterprises/getAll`, options);

    const enterprises = await response.json();

    const response3 = await fetch(`/api/auths/studentsWithInternship:2023-2024`, options);
    const NbWithInternships = await response3.json();
    
    const response4 = await fetch(`/api/auths/studentsWithoutInternship:2023-2024`, options);
    const NbWithoutInternships = await response4.json();

    const main = document.querySelector('main');
    main.innerHTML = `
    <section>
        <div class="container h-100">
            <div class="row d-flex justify-content-center align-items-center h-100">
                <div class="col-12 text-center">
                    <h1>Tableau de bord</h1>
                </div>
                
            </div>

            <style>
                #myChart {
                    max-width: 400px;
                    max-height: 400px;
                }
            </style>
            <div class="container h-100">
                <div class="row d-flex justify-content-center align-items-center h-100">
                    <div class="col-md-6 text-center">
                        <canvas id="myChart" width="400" height="400"></canvas> <!-- Canvas element for the chart -->
                    </div>
                    <div class="col-md-6">
                        <table class="tableOfInternships">
                            <tbody>
                                <tr>
                                    <th>Total d'étudiants: </th>
                                    <td>${NbWithInternships + NbWithoutInternships}</td>
                                </tr>
                                <tr>
                                    <th>Nombre d'étudiants avec stage:</th>
                                    <td>${NbWithInternships}</td>
                                </tr>
                                <tr>
                                    <th>Nombre d'étudiants sans stage:</th>
                                    <td>${NbWithoutInternships}</td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>

            <p>Voici la liste de tout les entreprises :</p>

            <table class="table">
                <thead>
                    <tr>
                        <th>Entreprise</th>
                        <th>Appelation</th>
                        <th>Adresse</th>
                        <th>Numéro de téléphone</th>
                        <th>Ville</th>
                        <th>Nombre d'étudiant en stage</th>
                        <th>Email</th>
                        <th>Black-listée</th>
                        <th>Profil</th>
                    </tr>
                </thead>
                <tbody>
                </tbody>
            </table>
        </div>
    </section>`;



    // Create a new chart
    const ctx = document.getElementById('myChart').getContext('2d');
    // eslint-disable-next-line no-new
    new Chart(ctx, {
        type: 'pie', // type of chart
        data: {
            labels: ['Étudiants avec stage', 'Étudiants sans stage'],
            datasets: [{
                label: '# ', // the label
                data: [NbWithInternships, NbWithoutInternships], // the data
            }]
        },
        options: {
        }
    });
    
    
    const enterprisesTable = document.querySelector('.table tbody');

    enterprises.forEach( async enterprise => {
        const options2 = {
            method: 'GET',
            headers : {
                'Content-Type': 'application/json',
                "Authorization": `${getAuthenticatedUser().token}`,
            },
        };
        // Fetch the number of internships for this enterprise
        const response2 = await fetch(`/api/enterprises/getNbInternships:${enterprise.id}`, options2);
        const internshipCount = await response2.text();
    
        // Create a new row
        const row = document.createElement('tr');
    
        // Add the enterprise data to the row
        row.innerHTML = `
            <td>${enterprise.tradeName}</td>
            <td>${enterprise.designation}</td>
            <td>${enterprise.adresse}</td>
            <td>${enterprise.phoneNumber}</td>
            <td>${enterprise.city}</td>
            <td>${internshipCount}</td>
            <td>${enterprise.email}</td>
            <td>${enterprise.blackListed}</td>
            <td></td>
        `;
    
        // Create a new button
        const button = document.createElement('button');
        button.id = `profilButton${enterprise.id}`;
        button.className = 'btn btn-primary';
        button.textContent = 'consulter Profil';
    
        // Add the event listener to the button
        button.addEventListener('click', (event) => {
            event.preventDefault();
            Navigate(`/enterprise?enterpriseId=${enterprise.id}`);
        });
    
        // Create a new cell
        const cell = document.createElement('td');

        // Add the button to the cell
        cell.appendChild(button);

        // Add the cell to the row
        row.appendChild(cell);

        // Add the row to the table
        enterprisesTable.appendChild(row);
    });

    
};

export default BoardPage;
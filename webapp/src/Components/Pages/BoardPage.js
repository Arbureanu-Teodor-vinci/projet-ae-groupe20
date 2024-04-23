/* eslint-disable import/no-extraneous-dependencies */
// eslint-disable-next-line import/no-unresolved
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

    // Fetch all academic years
    const responseAcademicYears = await fetch('/api/academicYear/all', options);
    const academicYears = await responseAcademicYears.json();

    const response = await fetch(`/api/enterprises/getAll`, options);

    const enterprises = await response.json();

    // Fetch data for all academic years by default
    const response3 = await fetch(`/api/auths/studentsWithInternship`, options);
    const NbWithInternships = await response3.json();

    const response4 = await fetch(`/api/auths/studentsWithoutInternship`, options);
    const NbWithoutInternships = await response4.json();

    const main = document.querySelector('main');
    main.innerHTML = `
    <section>
        <div style="margin-top: 100px" class="container h-100">
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
                        <select id="academicYear">
                            <option value="All Years">Tous les années</option>
                            ${academicYears.reverse().map(year => `<option value="${year}">${year}</option>`).join('')}
                        </select>
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
                        <th>Nombre d'étudiants en stage</th>
                        <th>Email</th>
                        <th>Raison de blacklist</th>
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

    let myChart = null; // Declare the 'myChart' variable

    myChart = new Chart(ctx, {
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


    // Add an event listener to the academic year dropdown
    const academicYearSelect = document.getElementById('academicYear');
    academicYearSelect.addEventListener('change', async () => {
        const selectedYear = academicYearSelect.value;

        let NbWithInternships2;
        let NbWithoutInternships2;

        if (selectedYear === "All Years") {
            // Fetch data for all years
            const responseAll1 = await fetch(`/api/auths/studentsWithInternship`, options);
            NbWithInternships2 = await responseAll1.json();
        
            const responseAll2 = await fetch(`/api/auths/studentsWithoutInternship`, options);
            NbWithoutInternships2 = await responseAll2.json();
        } else{
            // Fetch data for the selected academic year
            const response5 = await fetch(`/api/auths/studentsWithInternship:${selectedYear}`, options);
            NbWithInternships2 = await response5.json();

            const response6 = await fetch(`/api/auths/studentsWithoutInternship:${selectedYear}`, options);
            NbWithoutInternships2 = await response6.json();
        }

        // Update the chart data
        myChart.data.datasets[0].data = [NbWithInternships2, NbWithoutInternships2];
        myChart.update();

        // Update the table data
        document.querySelector('.tableOfInternships tbody').innerHTML = `
            <tr>
                <th>Total d'étudiants: </th>
                <td>${NbWithInternships2 + NbWithoutInternships2}</td>
            </tr>
            <tr>
                <th>Nombre d'étudiants avec stage:</th>
                <td>${NbWithInternships2}</td>
            </tr>
            <tr>
                <th>Nombre d'étudiants sans stage:</th>
                <td>${NbWithoutInternships2}</td>
            </tr>
        `;
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
            <td${enterprise.blackListed ? ' style="color: red;"' : ''}>${enterprise.tradeName}</td>
            <td${enterprise.blackListed ? ' style="color: red;"' : ''}>${enterprise.designation}</td>
            <td${enterprise.blackListed ? ' style="color: red;"' : ''}>${enterprise.address}</td>
            <td${enterprise.blackListed ? ' style="color: red;"' : ''}>${enterprise.phoneNumber}</td>
            <td${enterprise.blackListed ? ' style="color: red;"' : ''}>${enterprise.city}</td>
            <td${enterprise.blackListed ? ' style="color: red;"' : ''}>${internshipCount}</td>
            <td${enterprise.blackListed ? ' style="color: red;"' : ''}>${enterprise.email}</td>
            <td${enterprise.blackListed ? ' style="color: red;"' : ''}>${enterprise.blackListMotivation == null ? '-' : enterprise.blackListMotivation}</td>
            <td${enterprise.blackListed ? ' style="color: red;"' : ''}>${enterprise.blackListed ? 'X' : 'V'}</td>
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
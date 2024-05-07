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

    const enterprises = await response.json()


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
                <select id="academicYear">
                    <option value="All Years">Tous les années</option>
                    ${academicYears.reverse().map(year => `<option value="${year}">${year}</option>`).join('')}
                </select>
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
                        <th data-column="tradeName" data-sort-order="asc">Entreprise</th>
                        <th data-column="designation" data-sort-order="asc">Appelation</th>
                        <th data-column="address" data-sort-order="asc">Adresse</th>
                        <th data-column="phoneNumber" data-sort-order="asc">Numéro de téléphone</th>
                        <th data-column="city" data-sort-order="asc">Ville</th>
                        <th data-column="internshipCount" data-sort-order="asc">Nombre d'étudiants en stage</th>
                        <th data-column="email" data-sort-order="asc">Email</th>
                        <th data-column="blackListReason" data-sort-order="asc">Raison de blacklist</th>
                        <th data-column="blackList" data-sort-order="asc">Black-listée</th>
                        <th data-column="profile" data-sort-order="asc">Profil</th>
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

    // Function to create a row for an enterprise
    async function createRow(enterprise) {
        // Create a new row
        const row = document.createElement('tr');

        // Add the enterprise data to the row
        row.innerHTML = `
            <td${enterprise.blackListed ? ' style="color: red;"' : ''}>${enterprise.tradeName}</td>
            <td${enterprise.blackListed ? ' style="color: red;"' : ''}>${enterprise.designation}</td>
            <td${enterprise.blackListed ? ' style="color: red;"' : ''}>${enterprise.address}</td>
            <td${enterprise.blackListed ? ' style="color: red;"' : ''}>${enterprise.phoneNumber}</td>
            <td${enterprise.blackListed ? ' style="color: red;"' : ''}>${enterprise.city}</td>
            <td${enterprise.blackListed ? ' style="color: red;"' : ''}>${enterprise.internshipCount}</td>
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

        return row;
    }

    // Sort the enterprises by trade name
    enterprises.sort((a, b) => a.tradeName.localeCompare(b.tradeName));

    const enterprisesTable = document.querySelector('.table tbody');

    let enterprisesWithInternshipCount = []; // Define it in the outer scope
    let rows = []; // Define it in the outer scope

    async function updateTable() {
        // Fetch the internshipCount for all enterprises
        enterprisesWithInternshipCount = await Promise.all(enterprises.map(async (enterprise) => {
            const options2 = {
                method: 'GET',
                headers : {
                    'Content-Type': 'application/json',
                    "Authorization": `${getAuthenticatedUser().token}`,
                },
            };
            const selectedYear = academicYearSelect.value; // Get the selected year
            let response2;
            if (selectedYear === "All Years") {
                // Fetch total internships for all years
                response2 = await fetch(`/api/enterprises/getNbInternships:${enterprise.id}`, options2);
            } else {
                // Fetch internships for the selected year
                response2 = await fetch(`/api/enterprises/getNbInternshipsPerAcademicYear:${enterprise.id}:${selectedYear}`, options2);
            }
            const internshipCount = await response2.json();
            return { ...enterprise, internshipCount };
        }));

        // Clear the table
        enterprisesTable.innerHTML = '';

        // Create an array to store the rows
        rows = [];

        await Promise.all(enterprisesWithInternshipCount.map( async (enterprise, index) => {
            // Create the row
            const row = await createRow(enterprise);

            // Store the row in the array
            rows[index] = row;
        }));

        // Append the rows to the table in the correct order
        rows.forEach(row => enterprisesTable.appendChild(row));
    }

    // Call the function to initially populate the table
    updateTable();

    // Add an event listener to the academic year select element
    academicYearSelect.addEventListener('change', updateTable);

    // Get the table headers
    const headers = document.querySelectorAll('.table thead th');

    // Define the sorting functions for each column
    const sortFunctions = {
        tradeName: (a, b) => a.tradeName.localeCompare(b.tradeName),
        designation: (a, b) => (a.designation || '').localeCompare(b.designation || ''),
        address: (a, b) => a.address.localeCompare(b.address),
        phoneNumber: (a, b) => a.phoneNumber.localeCompare(b.phoneNumber),
        city: (a, b) => a.city.localeCompare(b.city),
        internshipCount: (a, b) => a.internshipCount - b.internshipCount,
        email: (a, b) => {
            if (a.email && b.email) {
                return a.email.localeCompare(b.email);
            }
            if (a.email) {
                return -1;
            }
            if (b.email) {
                return 1;
            }
            return 0;
        },
        blackListReason: (a, b) => (a.blackListMotivation || '').localeCompare(b.blackListMotivation || ''),
        blackList: (a, b) => (a.blackListed === true ? 1 : 0) - (b.blackListed === true ? 1 : 0),
    };

    // Create a sortOrder object to keep track of the sort order for each header
    const sortOrder = {};

    // Add the event listeners to the headers
    headers.forEach(header => {
        header.addEventListener('click', () => {
            // Get the sort function for this column
            const sortFunction = sortFunctions[header.dataset.column];

            // Get the current sort order
            sortOrder[header.dataset.column] = sortOrder[header.dataset.column] || 'asc';

            // Flip the sort order
            const newSortOrder = sortOrder[header.dataset.column] === 'asc' ? 'desc' : 'asc';

            // Sort the enterprises
            enterprisesWithInternshipCount.sort((a, b) => {
                const sortValue = sortFunction(a, b);
                // Reverse the sort order if necessary
                return sortOrder[header.dataset.column] === 'asc' ? sortValue : -sortValue;
            });


            // Clear the table
            while (enterprisesTable.firstChild) {
                enterprisesTable.removeChild(enterprisesTable.firstChild);
            }

            // Rebuild the table
            Promise.all(enterprisesWithInternshipCount.map(async (enterprise, index) => {
                // Create the row
                const row = await createRow(enterprise);

                // Store the row in the array
                rows[index] = row;
            })).then(() => {
                // Append the rows to the table in the correct order
                rows.forEach(row => enterprisesTable.appendChild(row));
            });

            // Update the sort order
            sortOrder[header.dataset.column] = newSortOrder;
        });
    });

};

export default BoardPage;
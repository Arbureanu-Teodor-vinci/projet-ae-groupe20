import { clearPage } from "../../utils/render";
import { getAuthenticatedUser } from '../../utils/auths';

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

    

    const main = document.querySelector('main');
    main.innerHTML = `
    <section>
        <div class="container h-100">
            <div class="row d-flex justify-content-center align-items-center h-100">
                <div class="col-12 text-center">
                    <h1>Tableau de bord</h1>
                </div>
                <p>Voici la liste de tout les entreprises :</p>
            </div>
            <table class="table">
                <thead>
                    <tr>
                        <th>Entreprise</th>
                        <th>Appelation</th>
                        <th>Adresse</th>
                        <th>Numéro de téléphone</th>
                        <th>Ville</th>
                        <th>Email</th>
                        <th>Black-listée</th>
                    </tr>
                </thead>
                <tbody>
                </tbody>
            </table>
        </div>
    </section>`;

    const enterprisesTable = document.querySelector('.table tbody');
    enterprises.forEach(enterprise => {
        enterprisesTable.innerHTML += `
          <tr>
            <td>${enterprise.tradeName}</td>
            <td>${enterprise.designation}</td>
            <td>${enterprise.adresse}</td>
            <td>${enterprise.phoneNumber}</td>
            <td>${enterprise.city}</td>
            <td>${enterprise.email}</td>
            <td>${enterprise.blackListed}</td>
        </tr>
        `;
    }
    );
};

export default BoardPage;
import { clearPage } from "../../utils/render";
import { getAuthenticatedUser } from "../../utils/auths";

const EnterprisePage = async () => {
    clearPage();
    
    // Get the current URL
    const url = new URL(window.location.href);

    // Get the enterpriseId parameter from the URL
    const enterpriseId = url.searchParams.get('enterpriseId');

    // Pass the enterpriseId to the render function
    await renderEnterprisePage(enterpriseId);
}

async function renderEnterprisePage(enterpriseId) {

    const options = {
        method: 'GET',
        headers : {
            'Content-Type': 'application/json',
            "Authorization": `${getAuthenticatedUser().token}`,
        },
    };
    // Fetch the number of internships for this enterprise
    const response = await fetch(`/api/enterprises/getOne:${enterpriseId}`, options);
    const enterprise = await response.json();

    const response2 = await fetch(`/api/contacts/getByEnterprise:${enterpriseId}`, options);

    const contacts = await response2.json();

    const main = document.querySelector('main');
    main.innerHTML = `
    <section>
        <div class="container h-100">
            <div class="row d-flex justify-content-center align-items-center h-100">
                <div class="col-12 text-center">
                    <h1>${enterprise.tradeName}</h1>
                </div>
                <p>Voici la liste de tout les contacts :</p>
            </div>
            <table class="table">
                <thead>
                    <tr>
                        <th>Etudiant</th>
                        <th>Etat du contact</th>
                        <th>Moyen de contact</th>
                        <th>Outil de contact</th>
                        <th>Raison de refus</th>
                    </tr>
                </thead>
                <tbody>
                </tbody>
            </table>
        </div>
    </section>`;

    const contactsTable = document.querySelector('.table tbody');
    contacts.forEach(async contact => {

        const response3 = await fetch(`/api/auths/user:${contact.studentId}`, options);
        const student = await response3.json();

        contactsTable.innerHTML += `
            <tr>
                <td>${student.lastName} ${student.firstName}</td>
                <td>${contact.stateContact}</td>
                <td>${contact.interviewMethod}</td>
                <td>${contact.tool}</td>
                <td>${contact.refusalReason}</td>
            </tr>
        `;
    });
};

export default EnterprisePage;
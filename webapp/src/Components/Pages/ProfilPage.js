import { clearPage } from '../../utils/render';
import Navigate from '../Router/Navigate';
import { getAuthenticatedUser } from '../../utils/auths';

const ProfilPage = async () => {
    if (!getAuthenticatedUser()) {
        Navigate('/login');
        return;
      };

    clearPage();
    await renderProfilPage();
};

async function renderProfilPage() {
    const user = getAuthenticatedUser();
    const options = {
        method: 'GET',
        headers : {
          'Content-Type': 'application/json',
          'Authorization': `${getAuthenticatedUser().token}`
        },
      };
      const response = await fetch(`/api/contacts/getByUser:${user.id}`, options);
      const contacts = await response.json();    
      
    const main = document.querySelector('main');
    main.innerHTML = `
    <section>
    <div class="container h-100">
        <div class="row d-flex justify-content-center align-items-center h-100">
            <div class="col-12 text-center">
                <h1>Mon Profil</h1>
            </div>
        </div>
        <div class="d-flex justify-content-between align-items-center">
            <h3>Mes données personnelles</h3>
            <button id="editButton" class="btn btn-primary">Modifier</button>
        </div>
        <div class="col-12 mt-3">
            <table class="table table-bordered">
                <tbody>
                    <tr>
                        <th>Nom</th>
                        <td>${user.lastName}</td>
                    </tr>
                    <tr>
                        <th>Prénom</th>
                        <td>${user && user.firstName}</td>
                    </tr>
                    <tr>
                        <th>Email</th>
                        <td>${user && user.email}</td>
                    </tr>
                    <tr>
                        <th>Tel</th>
                        <td>${user && user.phoneNumber}</td>
                    </tr>
                </tbody>
            </table>
        </div>
        ${user && user.role === 'Etudiant' ? `
        <div class="row mt-5">
            <div class="d-flex justify-content-between align-items-center">
                <h3>Mon Stage</h3>
                <button id="editButton" class="btn btn-primary">Modifier mon sujet de stage</button>
            </div>
            <div class="col-12 mt-3">
                <table class="table table-bordered">
                    <tbody>
                        <tr>
                            <th>Entreprise</th>
                            <td class="text-center">${user && user.internship && user.internship.company || ' - '}</td>
                        </tr>
                        <tr>
                            <th>Responsable de stage</th>
                            <td class="text-center">${user && user.internship && user.internship.internshipManager || ' - '}</td>
                        </tr>
                        <tr>
                            <th>Sujet du stage</th>
                            <td class="text-center">${user && user.internship && user.internship.internshipSubject || ' - '}</td>
                        </tr>
                        <tr>
                            <th>Date de signature du stage</th>
                            <td class="text-center">${user && user.internship && user.internship.internshipSignatureDate || ' - '}</td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
        <div class="row mt-5">
            <div class="d-flex justify-content-between align-items-center">
                <h3>Contacts</h3>
                <button id="creationContact" class="btn btn-primary">Ajouter un nouveau contact</button>
            </div>
            <div class="col-12 mt-3">
                <div class="table-responsive">
                    <table class="table table-bordered">
                        <thead>
                            <tr>
                                <th>Entreprise</th>
                                <th>Moyen de contact</th>
                                <th>Outil de contact</th>
                                <th>Etat du contact</th>
                                <th>Raison du refus</th>
                            </tr>
                        </thead>
                        <tbody>
                            ${contacts.map(contact => `
                            <tr>
                                <td class="text-center">${contact.enterpriseId}</td>
                                <td class="text-center">${contact.interViewMethod || ' - '}</td>
                                <td class="text-center">${contact.tool || ' - '}</td>
                                <td class="text-center">${contact.stateContact || ' - '}</td>
                                <td class="text-center">${contact.refusalReason || ' - '}</td>
                                <td class="text-center">
                                    <button id="editButton${contact.id}" class="btn btn-primary">Modifier</button>
                                </td>
                            </tr>
                            `).join('')}
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
        ` : ''}
    </div>
</section>`

    contacts.forEach(contact => {
        const button = document.getElementById(`editButton${contact.id}`);
        button.addEventListener('click', () => {
            // Enregistrez l'ID du contact dans le localStorage
            localStorage.setItem('contactIdToEdit', contact.id);
            // Redirigez l'utilisateur vers la page de modification
            window.location.href = '/updateContact';
        });
    });

    const link = document.querySelector('#editButton');
    link.addEventListener('click', (e) => {
        e.preventDefault();
        Navigate('/modification');
    });

    if (user.role === 'Etudiant') {
    const linkContact = document.querySelector('#creationContact');
    linkContact.addEventListener('click', (e) => {
        e.preventDefault();
        Navigate('/creationContact');
    });
}
}

export default ProfilPage;
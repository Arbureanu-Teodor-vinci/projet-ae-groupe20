import { clearPage } from '../../utils/render';
import Navigate from '../Router/Navigate';
import { getAuthenticatedUser } from '../../utils/auths';

const StudentInfoPage = async () => {
    if (!getAuthenticatedUser()){
        Navigate('/login');
        return;
    }

    clearPage();
    await renderStudentInfoPage();
};

async function renderStudentInfoPage() {
    if (getAuthenticatedUser().role === 'Etudiant'){
        Navigate('/');
        return;
    }
    const urlParams = new URLSearchParams(window.location.search);
    const studentId = urlParams.get('userId');
    // eslint-disable-next-line no-console
    console.log(studentId);

    const options = {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `${getAuthenticatedUser().token}`,
        },
    };

    const response = await fetch(`/api/auths/user:${studentId}`, options);
    const responseContacts = await fetch(`/api/contacts/getByUser:${studentId}`, options);


    if (response.ok) {
        const user = await response.json();
        // eslint-disable-next-line no-console
        if(user.role !== 'Etudiant'){
            Navigate('/');
            return;
        }
    
    if (responseContacts.ok) {
        const contacts = await responseContacts.json();
    
        // Récupérez les détails de l'entreprise pour chaque contact
        const entreprisePromises = contacts.map(contact => fetch(`/api/enterprises/getOne:${contact.enterpriseId}`, options));
    
        // Recupérez les réponses de chaque requête
        const entrepriseResponses = await Promise.all(entreprisePromises);
    
        // Récupérez les détails de l'entreprise pour chaque contact
        // eslint-disable-next-line no-shadow
        const contactsWithEnterprise = await Promise.all(entrepriseResponses.map(async (responseContacts, i) => {
            if (responseContacts.ok) {
                const entreprise = await responseContacts.json();
                // Ajoutez les détails de l'entreprise au contact
                return { ...contacts[i], entreprise };
            } 
                // eslint-disable-next-line no-console
                console.log(`Erreur lors de la récupération de l'entreprise : ${responseContacts.statusText}`);
                return contacts[i];
            
        }));
      
    const main = document.querySelector('main');
    main.innerHTML = `
    <section>
    <div style="margin-top: 100px" class="container h-100">
        <div class="row d-flex justify-content-center align-items-center h-100">
            <div class="col-12 text-center">
                <h1>Profil de ${user.lastName} ${user.firstName}</h1>
            </div>
        </div>
        <div class="d-flex justify-content-between align-items-center">
            <h3>Données personnelles de l'étudiant</h3>
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
                        <td>${user.firstName}</td>
                    </tr>
                    <tr>
                        <th>Email</th>
                        <td>${user.email}</td>
                    </tr>
                    <tr>
                        <th>Tel</th>
                        <td>${user.telephoneNumber}</td>
                    </tr>
                </tbody>
            </table>
        </div>
        <div class="row mt-5">
            <div class="d-flex justify-content-between align-items-center">
                <h3>Mon Stage</h3>
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
                        ${contactsWithEnterprise.map(contact => `
                        <tr>
                            <td class="text-center">${contact.entreprise.tradeName}</td>
                            <td class="text-center">${contact.interViewMethod || ' - '}</td>
                            <td class="text-center">${contact.tool || ' - '}</td>
                            <td class="text-center">${contact.stateContact || ' - '}</td>
                            <td class="text-center">${contact.refusalReason || ' - '}</td>
                        </tr>
                        `).join('')}
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</section>`
        }
    }
}
export default StudentInfoPage;
    
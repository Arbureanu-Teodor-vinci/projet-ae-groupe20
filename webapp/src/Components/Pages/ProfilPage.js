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
            "Authorization": `${getAuthenticatedUser().token}`,
        },
    };
    
    const response = await fetch(`/api/contacts/getByUser`, options);
    
    if (response.ok) {
        const contacts = await response.json();
    
        // Créez un tableau de promesses pour chaque contact
        const entreprisePromises = contacts.map(contact => fetch(`/api/enterprises/getOne:${contact.enterpriseId}`, options));
    
        // Attendez que toutes les promesses soient résolues
        const entrepriseResponses = await Promise.all(entreprisePromises);
    
        // Récupérez les détails de l'entreprise pour chaque contact
        // eslint-disable-next-line no-shadow
        const contactsWithEnterprise = await Promise.all(entrepriseResponses.map(async (response, i) => {
            if (response.ok) {
                const entreprise = await response.json();
                // Ajoutez les détails de l'entreprise au contact
                return { ...contacts[i], entreprise };
            } 
                // eslint-disable-next-line no-console
                console.log(`Erreur lors de la récupération de l'entreprise : ${response.statusText}`);
                return contacts[i];
            
        }));
      
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
                        <td>${user && user.telephoneNumber}</td>
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
                                <th></th>
                                <th></th>
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
                            <td class="text-center">
                                <button id="editButton${contact.id}" class="btn btn-primary">Modifier</button>
                            </td>
                            <td class="text-center">
                                ${contact.stateContact === 'accepté' ? `<button id="${contact.id}" class="btn btn-success addStage">Ajouter un stage</button>` : ''}
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
    const linkContact = document.querySelector('#creationContact');

    contacts.forEach(contact => {
        const button = document.getElementById(`editButton${contact.id}`);
        if(contact.stateContact === 'accepté' || contact.stateContact === 'refusé' || contact.stateContact === 'suspendu' || contact.stateContact === 'non suivis') {
            button.disabled = true;
        }else{
            button.addEventListener('click', () => {
                Navigate(`/updateContact?contactId=${contact.id}`);
            });
        }
        if(contact.stateContact === 'accepté')
           linkContact.disabled = true;
    });

    const addStageButtons = document.querySelector('.addStage');
    if (addStageButtons){
        addStageButtons.addEventListener('click', (e) => {
            e.preventDefault();
            Navigate(`/creationStage?contactId=${e.target.id}`);
        });
    }
    
    const link = document.querySelector('#editButton');
    link.addEventListener('click', (e) => {
        e.preventDefault();
        Navigate('/updateUserInfos');
    });

        if (user.role === 'Etudiant') {
        linkContact.addEventListener('click', (e) => {
            e.preventDefault();
            Navigate('/creationContact');
            });
        }
}
}

export default ProfilPage;
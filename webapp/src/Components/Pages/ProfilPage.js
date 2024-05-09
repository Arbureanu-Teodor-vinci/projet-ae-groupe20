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
    
    const response = await fetch(`/api/contacts/getByUser:${user.id}`, options);
    
    if (response.ok) {
        const contacts = await response.json();

    const main = document.querySelector('main');
    main.innerHTML = `
    <section>
    <div style="margin-top: 100px" class="container h-100">
        <div class="row d-flex justify-content-center align-items-center h-100">
            <div class="col-12 text-center">
                <h1>Mon Profil</h1>
            </div>
        </div>
        <div class="d-flex justify-content-between align-items-center">
            <h3>Mes données personnelles</h3>
            <button id="editUserInfos" class="btn btn-primary">Modifier mes données</button>
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
        ${
          user.role === 'Etudiant'
            ? `
        <div class="row mt-5 internshipTable">
            <div class="d-flex justify-content-between align-items-center">
                <h3>Mon Stage</h3>
                <button id="editInternshipSubject" class="btn btn-primary">Modifier mon sujet de stage</button>
            </div>
            <div class="col-12 mt-3">
                <table class="table table-bordered">
                    <tbody>
                        <tr>
                            <th>Entreprise</th>
                            <td class="text-center" id ="internshipEnterprise"> - </td>
                        </tr>
                        <tr>
                            <th>Responsable de stage</th>
                            <td class="text-center" id="internshipSupervisor"> - </td>
                        </tr>
                        <tr>
                            <th>Sujet du stage</th>
                            <td class="text-center" id="internshipSubject"> - </td>
                        </tr>
                        <tr>
                            <th>Date de signature du stage</th>
                            <td class="text-center" id="signatureDate"> - </td>
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
                        ${contacts
                            .map(
                                (contact) => `
                        <tr>
                                <td class="text-center" style="color: ${
                                    contact.enterprise.blackListed ? 'red' : 'black'
                                }; font-weight: ${
                                    contact.enterprise.blackListed ? 'bold' : 'normal'
                                }">${contact.enterprise.tradeName} ${contact.entreprise.designation ? ` - ${contact.entreprise.designation}` : ''}</td>
                                <td class="text-center">${contact.interViewMethod || ' - '}</td>
                                <td class="text-center">${contact.tool || ' - '}</td>
                                <td class="text-center">${contact.stateContact || ' - '}</td>
                                <td class="text-center">${contact.refusalReason || ' - '}</td>
                                <td class="text-center">
                                        <button id="editButton${
                                            contact.id
                                        }" class="btn btn-primary">Modifier</button>
                                </td>
                                <td class="text-center">
                                        ${
                                            contact.stateContact === 'accepté'
                                                ? `<button id="${contact.id}" class="btn btn-success addStage">Ajouter un stage</button>`
                                                : ''
                                        }
                                </td>
                        </tr>
                        `,
                            )
                            .join('')}
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
        `
            : ''
        }
    </div>
</section>`;
    const linkContact = document.querySelector('#creationContact');
    const internshipSubject = document.getElementById('internshipSubject');
    const editInternshipSubjectButton = document.getElementById('editInternshipSubject');

    let acceptedContact = false;
    contacts.forEach(contact => {
        const button = document.getElementById(`editButton${contact.id}`);
        if(contact.stateContact === 'accepté' || contact.stateContact === 'refusé' || contact.stateContact === 'suspendu' || contact.stateContact === 'non suivis') {
            button.disabled = true;
        }else{
            button.addEventListener('click', () => {
                Navigate(`/updateContact?contactId=${contact.id}`);
            });
        }
        if(contact.stateContact === 'accepté'){
            linkContact.disabled = true;
            acceptedContact = true;
        }
           
    });

    if(acceptedContact){
        linkContact.disabled = true;
        contacts.forEach(contact => {
                const button = document.getElementById(`editButton${contact.id}`);
                button.disabled = true;
        });
        const responseInternship = await fetch(`/api/internships/getOneInternshipByStudentId:${user.id}`, options);
        let internship = null;
        if(responseInternship.ok){
            internship = await responseInternship.json();
            if(internship){
                const responseInternshipSupervisor = await fetch(`/api/supervisors/getOne:${internship.supervisorId}`, options);
                const internshiptSupervisor = await responseInternshipSupervisor.json();
                internshipSubject.textContent = internship.subject;
                document.getElementById('signatureDate').textContent = internship.signatureDate;
                const {enterprise} = contacts.find(contact => contact.id === internship.contactId);
                const internshipEnterpriseTableLine = document.getElementById('internshipEnterprise');
                internshipEnterpriseTableLine.textContent = enterprise.tradeName;
                if (enterprise.blackListed) {
                    internshipEnterpriseTableLine.style.color = 'red';
                    internshipEnterpriseTableLine.style.fontWeight = 'bold';
                    internshipEnterpriseTableLine.textContent += ' (blacklisted)';
                }
                document.getElementById('internshipSupervisor').textContent = `${internshiptSupervisor.firstName  } ${  internshiptSupervisor.lastName}`;
                const addStageButtons = document.querySelector('.addStage');
                addStageButtons.disabled = true;
            }
        }

        editInternshipSubjectButton.addEventListener('click', async () => {
            // Si le bouton dit 'Modifier mon sujet de stage', changez le sujet de stage en un champ de saisie
          // et changez le texte du bouton en 'Envoyer modification'
          if (editInternshipSubjectButton.textContent === 'Modifier mon sujet de stage') {
              const subjectText = internshipSubject.textContent;
              internshipSubject.innerHTML = `<input type="text" id="subjectInput" value="${subjectText}">`;
              editInternshipSubjectButton.textContent = 'Envoyer modification';
              document.getElementById('subjectInput').focus();
          } 
          // Si le bouton dit 'Envoyer modification', changez le champ de saisie en texte
          // et changez le texte du bouton en 'Modifier mon sujet de stage'
          else if (editInternshipSubjectButton.textContent === 'Envoyer modification') {
              const subjectInput = document.getElementById('subjectInput');
              const responseUpdateInternship = await fetch(`/api/internships/updateSubject`, {
                method: 'PATCH',
                headers: {
                    'Content-Type': 'application/json',
                    Authorization: `${getAuthenticatedUser().token}`,
                },
                body: JSON.stringify({ 
                  id: internship.id,
                  subject: subjectInput.value,
                  academicYear: internship.academicYear,
                  version: internship.version,
                  contactId: internship.contactId,
                  supervisorId: internship.supervisorId,
                  signatureDate: internship.signatureDate, }),
            });
            if (responseUpdateInternship.ok) {
                internshipSubject.textContent = document.getElementById('subjectInput').value;
                internship = await responseUpdateInternship.json();
            }else{
              // eslint-disable-next-line no-alert
              alert(`${responseUpdateInternship.status} : ${await responseUpdateInternship.text()}`);
              Navigate('/profil');
            }
              editInternshipSubjectButton.textContent = 'Modifier mon sujet de stage';
          }
      
          });

         
    }else{
        const internshipTable = document.querySelector('.internshipTable');
        if (internshipTable) {
            internshipTable.style.display = 'none';
        }
    }

    

    const addStageButtons = document.querySelector('.addStage');
    if (addStageButtons){
        addStageButtons.addEventListener('click', (e) => {
            e.preventDefault();
            Navigate(`/creationStage?contactId=${e.target.id}`);
        });
    }
    
    const link = document.querySelector('#editUserInfos');
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
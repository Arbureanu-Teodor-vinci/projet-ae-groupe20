/* eslint-disable no-alert */
import { clearPage } from '../../utils/render';
import { getAuthenticatedUser } from '../../utils/auths';
import Navigate from '../Router/Navigate';

const main = document.querySelector('main');

const EnterprisePage = async () => {

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

    // Get the current URL
    const url = new URL(window.location.href);

    // Get the enterpriseId parameter from the URL
    const enterpriseId = url.searchParams.get('enterpriseId');

    // Pass the enterpriseId to the render function
    await renderEnterprisePage(enterpriseId);
  }
};

async function renderEnterprisePage(enterpriseId) {
  const options = {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
      Authorization: `${getAuthenticatedUser().token}`,
    },
  };
  // Fetch the number of internships for this enterprise
  const response = await fetch(`/api/enterprises/getOne:${enterpriseId}`, options);
  const enterprise = await response.json();

  const response2 = await fetch(`/api/contacts/getByEnterprise:${enterpriseId}`, options);

  const contacts = await response2.json();

  main.innerHTML = `
<section>
<div style="margin-top: 100px" class="container h-100">
    <div class="row d-flex justify-content-center align-items-center h-100">
        <div class="col-12 d-flex justify-content-between align-items-center">
          <h1 id="enterpriseTitle">${enterprise.tradeName}${enterprise.designation ? ` - ${enterprise.designation}` : ''}</h1>
        <div id="blacklistReasonDiv" >
            <div style="display: flex; align-items: center;">
                <input type="text" id="blacklistReason" name="blacklistReason" style="width: 700px;" placeholder="Raison de blacklist">
                <div style="width: 10px;"></div>
                <button id="blacklistButton" class="btn btn-danger">Blacklister</button>
            </div>
            <p class="errorMessage"></p>
        </div>
        </div>
        <p>Voici la liste de tous les contacts :</p>
        
    </div>
    <table class="table">
        <thead>
            <tr>
                <th>Etudiant</th>
                <th>Etat du contact</th>
                <th>Moyen de contact</th>
                <th>Outil de contact</th>
                <th>Raison de refus</th>
                <th>Année académique</th>
            </tr>
        </thead>
        <tbody>
        </tbody>
    </table>
</div>
</section>`;

  if (!enterprise.blackListed) {
    document.getElementById('blacklistButton').addEventListener('click', async () => {
      const bodyBlacklistUpdate = {
        id: enterprise.id,
        blackListed: true,
        blackListMotivation: document.getElementById('blacklistReason').value,
        version: enterprise.version,
        tradeName: enterprise.tradeName,
        address: enterprise.address,
        phoneNumber: enterprise.phoneNumber,
        city: enterprise.city,
      };
      const optionsBlacklist = {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          Authorization: `${getAuthenticatedUser().token}`,
        },
        body: JSON.stringify(bodyBlacklistUpdate),
      };
      const responseBlackList = await fetch(`/api/enterprises/blacklist`, optionsBlacklist);
      if (responseBlackList.status === 200) {
        Navigate('/board');
      }else{
        const errorMessage = await responseBlackList.text();
        alert(`${responseBlackList.status} : ${errorMessage}`);
        Navigate(`/enterprise?enterpriseId=${enterprise.id}`);
        
      }
    });
  }else{
    document.getElementById('blacklistReasonDiv').style.display = 'none';
    document.getElementById('enterpriseTitle').style.color = 'red';
    document.getElementById('enterpriseTitle').innerHTML += ' (Blacklistée)';
  }

  const contactsTable = document.querySelector('.table tbody');
  contacts.forEach(async (contact) => {
  
    contactsTable.innerHTML += `
            <tr>
                <td>${contact.student.lastName} ${contact.student.firstName}</td>
                <td>${contact.stateContact}</td>
                <td>${contact.interviewMethod}</td>
                <td>${contact.tool ? contact.tool : ' - '}</td>
                <td>${contact.refusalReason ? contact.refusalReason : ' - '}</td>
                <td>${contact.academicYear.year}</td>
            </tr>
        `;
  });
}

export default EnterprisePage;

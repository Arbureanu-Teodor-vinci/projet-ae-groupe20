import { getAuthenticatedUser } from "../../utils/auths"
import { clearPage } from "../../utils/render";
import Navigate from "../Router/Navigate";

const  HomePage = async () => {
    if(!getAuthenticatedUser()){
        Navigate('/login');
        return;
    };
    clearPage();
    await renderHomePage();
};

async function renderHomePage(){
  const user = getAuthenticatedUser();
  const main = document.querySelector('main');
  if(user.role === 'Etudiant'){
    const options = {
    method: 'GET',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `${getAuthenticatedUser().token}`,
    },
  };
  

  const response = await fetch(`/api/contacts/getByUser:${user.id}`, options);
  if(response.ok){
    const contacts = await response.json();
    // Verifiy if the student has accepted contacts
    // some method returns true if at least one element in the array passes the test
    const isContactAccepted = contacts.some(contact => contact.stateContact === 'accepté');

    // eslint-disable-next-line no-console
    console.log(contacts);
    main.innerHTML = `
      <section class="d-flex justify-content-center align-items-center" style="height: 50vh;">
        <div class="d-grid gap-2 col-6 mx-auto">
          <button id="profil" class="btn btn-primary mb-3" type="button">Voir mon profil</button>
          <button id="modifDataPerso" class="btn btn-primary mb-3" type="button">Modifier mes données personnelles</button>
          ${isContactAccepted ? '<button id="modifSubjectStage" class="btn btn-primary mb-3" type="button">Modifier mon sujet de stage</button>' : ''}
          ${isContactAccepted ? '' : '<button id="addContact" class="btn btn-primary mb-3" type="button">Ajouter un nouveau contact</button>'}
          ${isContactAccepted ? '' : '<button id="addCompany" class="btn btn-primary mb-3" type="button">Ajouter une nouvelle entreprise</button>'}
        </div>
      </section>    
      `;
    }
    const modifSubjectStageButton = document.querySelector('#modifSubjectStage');
    if(modifSubjectStageButton){
      modifSubjectStageButton.addEventListener('click', (e) => {
        e.preventDefault();
        Navigate('/profil');
      });
    }

    const addContactButton = document.querySelector('#addContact');
    if(addContactButton){
    addContactButton.addEventListener('click', (e) => {
      e.preventDefault();
      Navigate('/creationContact');
    });
  }
    const companyButton = document.querySelector('#companyButton');
    if(companyButton){
    companyButton.addEventListener('click', (e) => {
      e.preventDefault();
      Navigate('/creationCompany');
    });
    }
  };
  if(getAuthenticatedUser().role === 'Professeur' || getAuthenticatedUser().role === 'Administratif'){
    main.innerHTML = `
      <section class="d-flex justify-content-center align-items-center mt-5" style="height: 50vh;">
        <div class="d-grid gap-2 col-6 mx-auto">
          <button id="boardButton" class="btn btn-primary mb-3" type="button">Visualiser mon tableau de bord</button>
          <button id="profil" class="btn btn-primary mb-3" type="button">Visualiser mon profil</button>
          <button id="modifDataPerso" class="btn btn-primary mb-3" type="button">Modifier mes données personnelles</button>
          <button id="allUsersButton" class="btn btn-primary mb-3" type="button">Voir tout les utilisateurs</button>
        </div>
      </section>    
      `;

      const boardButton = document.querySelector('#boardButton');
      boardButton.addEventListener('click', (e) => {
        e.preventDefault();
        Navigate('/board');
      });   

      const allUsersButton = document.querySelector('#allUsersButton');
        allUsersButton.addEventListener('click', (e) => {
        e.preventDefault();
        Navigate('/allUsers');
      });
  };

  

  const profilButton = document.querySelector('#profil');
    profilButton.addEventListener('click', (e) => {
      e.preventDefault();
      Navigate('/profil');
    });


    const modifDataPersoButton = document.querySelector('#modifDataPerso');
    modifDataPersoButton.addEventListener('click', (e) => {
      e.preventDefault();
      Navigate('/updateUserInfos');
    });

    
};
        

export default HomePage;

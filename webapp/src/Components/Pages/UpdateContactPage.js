import { clearPage } from "../../utils/render";
// eslint-disable-next-line no-unused-vars
import Navigate from '../Router/Navigate';
import { getAuthenticatedUser } from '../../utils/auths';

const UpdateContactPage = async () => {
    clearPage();
    await renderUpdateContactPage();
}

async function renderUpdateContactPage() {
    const contactId = localStorage.getItem('contactIdToEdit');
    const options = {
        method: 'GET',
        headers : {
         'Content-Type': 'application/json',
          'Authorization': `${getAuthenticatedUser().token}`
        },
    };
    const response = await fetch(`/api/contacts/getOne:${contactId}`, options);
    const contact = await response.json();

    let stateOptions = '';
    let selectDisabled = '';
    if (['initié', 'pris'].includes(contact.stateContact)) {
        stateOptions = `
            <option value="${contact.stateContact}" selected>${contact.stateContact}</option>
            <option value="suspendu">Suspendu</option>
            <option value="non suivi">Non suivi</option>
        `;
    if (contact.stateContact === 'initié') {
            stateOptions += `<option value="pris">Pris</option>`;
        }
    if (contact.stateContact === 'pris') {
        stateOptions += `
            <option value="accepté">Accepté</option>
            <option value="refusé">Refusé</option>
        `;
    }

    } else if (['suspendu', 'accepté', 'refusé', 'non suivi'].includes(contact.stateContact)) {
        stateOptions = `<option value="${contact.stateContact}" selected>${contact.stateContact}</option>`;
        selectDisabled = 'disabled';
    }

    let refusalReasonDisabled = '';
    if (contact.stateContact !== 'refusé') {
        refusalReasonDisabled = 'disabled';
    }

    const main = document.querySelector('main');
    main.innerHTML = `
    <section>
        <div class="container h-100">
            <div class="row d-flex justify-content-center align-items-center h-100">
                <div class="col-12 text-center">
                    <h1>Modifier un contact</h1>
                    <form id="contactForm">
                        <div class="form-group">
                            <label for="enterpriseId">Entreprise</label>
                            <input type="text" id="enterpriseId" name="enterpriseId" class="form-control text-center" value="${contact.enterpriseId}">
                        </div>
                        <div class="form-group">
                            <label for="interViewMethod">Moyen de contact</label>
                            <select id="interViewMethod" name="interViewMethod" class="form-control text-center">
                                <option value="A distance" ${contact.interViewMethod === 'A distance' ? 'selected' : ''}>A distance</option>
                                <option value="Dans l'entreprise" ${contact.interViewMethod === 'Dans l\'entreprise' ? 'selected' : ''}>Dans l'entreprise</option>
                            </select>
                        </div>
                        <div class="form-group">
                            <label for="tool">Outil de contact</label>
                            <input type="text" id="tool" name="tool" class="form-control text-center" value="${contact.tool || ' - '}">
                        </div>
                        <div class="form-group">
                            <label for="stateContact">Etat du contact</label>
                            <select id="stateContact" name="stateContact" class="form-control text-center" ${selectDisabled}>
                                ${stateOptions}
                            </select>
                            ${selectDisabled ? '<p class="text-danger">Impossible de changer d\'état</p>' : ''}
                        </div>
                        <div class="form-group">
                            <label for="refusalReason">Raison de refus</label>
                            <input type="text" id="refusalReason" name="refusalReason" class="form-control text-center" value="${contact.refusalReason || ' '}" ${refusalReasonDisabled}>
                        </div>
                        <button type="submit" class="btn btn-primary" style="margin-top: 20px;">Modifier le contact</button>
                    </form>
                </div>
            </div>
        </div>
    </section>`;

    document.getElementById('contactForm').addEventListener('submit', (event) => {
        event.preventDefault();
        // Ajoutez ici le code pour mettre à jour le contact
        window.location.href = '/profil';
    });    
}


export default UpdateContactPage;
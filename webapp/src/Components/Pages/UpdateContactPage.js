import { clearPage } from "../../utils/render";
// eslint-disable-next-line no-unused-vars
import Navigate from '../Router/Navigate';
import { getAuthenticatedUser } from '../../utils/auths';

const UpdateContactPage = async () => {
    clearPage();
    await renderUpdateContactPage();
}

async function renderUpdateContactPage() {
    const main = document.querySelector('main');
    const urlParams = new URLSearchParams(window.location.search);
    const contactId = urlParams.get('contactId');
    const options = {
        method: 'GET',
        headers : {
         'Content-Type': 'application/json',
          'Authorization': `${getAuthenticatedUser().token}`
        },
    };
    const response = await fetch(`/api/contacts/getOne:${contactId}`, options);
    

    if(!response.ok){
        main.innerHTML = `
        <section>
            <div class="container h-100">
                <div class="row d-flex
                justify-content-center align-items-center h-100">
                    <div class="col-12 text-center">
                        <h1>${response.status} : ${response.statusText}</h1>
                    </div>
                </div>
            </div>
        </section>
    `;
    }else{

    const contact = await response.json();
    const responseEnterprise = await fetch(`/api/enterprises/getOne:${contact.enterpriseId}`, options);
    const enterprise = await responseEnterprise.json();
    // eslint-disable-next-line no-console
    console.log(enterprise.tradeName);

    let stateOptions = '';
    if (['initié', 'pris'].includes(contact.stateContact)) {
        stateOptions = `
            <option value="${contact.stateContact}" selected>${contact.stateContact}</option>
            <option value="non suivis">Non suivis</option>
            
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

    } else if (['accepté', 'refusé', 'non suivis','suspendu'].includes(contact.stateContact)) {
        stateOptions = `<option value="${contact.stateContact}" selected>${contact.stateContact}</option>`;
    }

    let nonUpdatable = '';
    if (contact.stateContact === 'refusé' || contact.stateContact === 'accepté' || contact.stateContact === 'suspendu' || contact.stateContact === 'non suivis') {
        nonUpdatable = 'disabled';
    }
    
    main.innerHTML = `
    <section>
        <div class="container h-100">
            <div class="row d-flex justify-content-center align-items-center h-100">
                <div class="col-12 text-center">
                    <h1>Modifier un contact</h1>
                    <form id="contactForm">
                        <div class="form-group">
                            <label for="enterpriseId"><B>Entreprise</B></label>
                            <input type="text" id="enterpriseId" name="enterpriseId" class="form-control text-center" value="${enterprise.tradeName}" ${nonUpdatable}>
                        </div>
                        <div class="form-group interviewMethodDiv">
                            <label for="interViewMethod"><B>Moyen de contact</B></label>
                            <select id="interViewMethod" name="interViewMethod" class="form-control text-center" ${nonUpdatable}>
                                <option value="A distance" ${contact.interViewMethod === 'A distance' ? 'selected' : ''}>A distance</option>
                                <option value="Dans l entreprise" ${contact.interViewMethod === 'Dans l entreprise' ? 'selected' : ''}>Dans l'entreprise</option>
                            </select>
                        </div>
                        <div class="form-group tool">
                            <label for="tool"><B>Outil de contact</B></label>
                            <input type="text" id="toolInput" name="tool" class="form-control text-center" value="${contact.tool === null ? '' : contact.tool}" ${nonUpdatable}>
                        </div>
                        <div class="form-group">
                            <label for="stateContact"><B>Etat du contact</B></label>
                            <select id="stateContact" name="stateContact" class="form-control text-center" ${nonUpdatable}>
                                ${stateOptions}
                            </select>
                        </div>
                        <div class="form-group refusalReason">
                            <label for="refusalReason"><B>Raison de refus</B></label>
                            <input type="text" id="refusalReasonInput" name="refusalReason" class="form-control text-center" value="${contact.refusalReason === null ? '' : contact.refusalReason}" ${nonUpdatable}>
                        </div>
                        ${nonUpdatable ? '<p class="text-danger">Vous ne pouvez pas changer l\'état du contact une fois qu\'il est passé à "accepté", "refusé", "suspendu" ou "non suivis"</p>' :
                        '<button id="updateContactButton" type="submit" class="btn btn-primary" style="margin-top: 20px;">Modifier le contact</button>'}
                    </form>
                </div>
            </div>
        </div>
    </section>`;

const interViewMethodSelectDiv = document.querySelector('.interviewMethodDiv');
const interViewMethodSelect = document.querySelector('#interViewMethod');
const stateContactSelect = document.querySelector('#stateContact');
const toolDiv = document.querySelector('.tool');
const toolInput = document.querySelector('#toolInput');
const refusalReasonDiv = document.querySelector('.refusalReason');
const refusalReasonInput = document.querySelector('#refusalReasonInput');

if (contact.interViewMethod === null) {
    document.querySelector('#interViewMethod').value = '';
}


// Hide the tool input if the interview method is not "A distance"
toolDiv.style.display = interViewMethodSelect.value === 'A distance' ? 'block' : 'none';

interViewMethodSelect.addEventListener('change', (event) => {
    // If the interview method is "A distance", display the tool input, otherwise hide it
    if (event.target.value === 'A distance') {
        toolDiv.style.display = 'block';
    } else {
        toolDiv.style.display = 'none';
        toolInput.value = null; 
    }
});

// Hide the refusal reason input if the state is not "refusé"
refusalReasonDiv.style.display = stateContactSelect.value === 'refusé' ? 'block' : 'none';

interViewMethodSelectDiv.style.display = (stateContactSelect.value === 'initié' || stateContactSelect.value === 'non suivis') ? 'none' : 'block';

stateContactSelect.addEventListener('change', (event) => {
    // If the state is "refusé", display the refusal reason input, otherwise hide it
    if (event.target.value === 'refusé') {
        refusalReasonDiv.style.display = 'block';
    } else {
        refusalReasonDiv.style.display = 'none';
        refusalReasonInput.value = null;
    }
    
    interViewMethodSelectDiv.style.display = (event.target.value === 'initié' || event.target.value === 'non suivis') ? 'none' : 'block';
    toolDiv.style.display = (event.target.value === 'initié' || event.target.value === 'non suivis' || interViewMethodSelect.value === 'Dans l entreprise') ? 'none' : 'block';
    
});

    document.getElementById('contactForm').addEventListener('submit', (event) => {
        event.preventDefault();
        Navigate('/profil');
    });
if(nonUpdatable === ''){
    document.getElementById("updateContactButton").addEventListener('click', async (event) => {
        event.preventDefault();
        const interviewMethod = (interViewMethodSelect.value === '' || interViewMethodSelect === "null") ? null  : interViewMethodSelect.value;
        const tool = (toolInput.value === '' || toolInput.value === "null") ? null : toolInput.value;
        const stateContact = stateContactSelect.value;
        const refusalReason = (refusalReasonInput.value === '' || refusalReasonInput.value === "null") ? null : refusalReasonInput.value;

        const body = {
            id: contactId,
            interviewMethod,
            tool,
            stateContact,
            refusalReason,
            academicYear: contact.academicYear,
            enterpriseId: contact.enterpriseId,
            studentId: contact.studentId,
            version: contact.version,
        };

        const optionsUpdateContact = {
            method: 'PATCH',
            headers : {
                'Content-Type': 'application/json',
                'Authorization': `${getAuthenticatedUser().token}`
            },
            body: JSON.stringify(body)
        };
        const responseUpdateContact = await fetch(`/api/contacts/update`, optionsUpdateContact);
        if (responseUpdateContact.status === 200 && stateContact === 'accepté') {    
            Navigate(`/creationStage?contactId=${contactId}`);
        } else if (responseUpdateContact.status === 200) {
            Navigate('/profil');
        } else {
            const errorMessage = await responseUpdateContact.text();
            main.innerHTML = `
            <section>
                <div class="container h-100">
                    <div class="row d-flex
                    justify-content-center align-items-center h-100">
                        <div class="col-12 text-center">
                            <h1>${responseUpdateContact.status} : ${errorMessage}</h1>
                        </div>
                    </div>
                </div>
            </section>
        `;
        }
    }
    );}

}

   

}


export default UpdateContactPage;
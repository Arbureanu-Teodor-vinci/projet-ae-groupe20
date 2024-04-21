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

    if (response.ok) {
        const student = await response.json();
        // eslint-disable-next-line no-console
        console.log(student);
    }
    
    const main = document.querySelector('main');
    main.innerHTML = `
    <section>
    <div style="margin-top: 100px" class="container h-100">
        <div class="row">
            <div class="col-md-12">
                <h1>Informations sur les étudiants</h1>
            <table class="table">
                <thead>
                    <tr>
                        <th>Email</th>
                        <th>Nom</th>
                        <th>Prénom</th>
                        <th>Numéro de téléphone</th>
                        <th>Date de création</th>
                        <th>Année académique</th>
                    </tr>
                </thead>
                <tbody>
                </tbody>
            </table>
            </div>
        </div>
    </div>
    </section>
        `;
}
export default StudentInfoPage;
    
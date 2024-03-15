import { clearPage } from '../../utils/render';

const StudentPage = async () => {
    clearPage();
    await renderStudentPage();
};

async function renderStudentPage() {
    const main = document.querySelector('main');
    main.innerHTML = `
    <section>
        <div class="container h-100">
            <div class="row d-flex justify-content-center align-items-center h-100">
                <div class="col-12 text-center">
                    <h1>Page Étudiant</h1>
                    <h2>Bienvenue sur votre espace étudiant</h2>
                    <p>Ici, vous pouvez consulter vos informations personnelles, vos données de stage et la liste de tous vos contacts pris.</p>
                </div>
            </div>
        </div>
    </section>`;
};

export default StudentPage;

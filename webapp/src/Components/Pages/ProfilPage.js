import { clearPage } from '../../utils/render';
import { getAuthenticatedUser } from '../../utils/auths';

const ProfilPage = async () => {
    clearPage();
    await renderProfilPage();
};

async function renderProfilPage() {
    const user = await getUser();

    const main = document.querySelector('main');
    main.innerHTML = `
    <section>
        <div class="container h-100">
            <div class="row d-flex justify-content-center align-items-center h-100">
                <div class="col-12 text-center">
                    <h1>Mon Profil</h1>
                </div>
            </div>
            <div class="row">
                <div class="col-12 d-flex justify-content-between align-items-center">
                    <h3>Mes données personnelles</h3>
                    <button class="btn btn-primary">Modifier</button>
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
                                <td>${user && user.phone_number}</td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </section>`;
}

async function getUser() {
    const options = {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
            "Authorization": `${getAuthenticatedUser().token}`
        },
    };

    const response = await fetch(`/api/auths`, options);
    return response.json();
}

export default ProfilPage;

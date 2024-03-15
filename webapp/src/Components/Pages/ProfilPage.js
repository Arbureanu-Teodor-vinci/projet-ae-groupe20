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
                    <button id="editButton" class="btn btn-primary" >Modifier</button>
                </div>
                <div class="col-12 mt-3">
                    <table class="table table-bordered">
                        <tbody>
                            <tr>
                                <th>Nom</th>
                                <td><!-- Ici, vous pouvez ajouter le nom de l'utilisateur --></td>
                            </tr>
                            <tr>
                                <th>Prénom</th>
                                <td><!-- Ici, vous pouvez ajouter le prénom de l'utilisateur --></td>
                            </tr>
                            <tr>
                                <th>Email</th>
                                <td><!-- Ici, vous pouvez ajouter l'email de l'utilisateur --></td>
                            </tr>
                            <tr>
                                <th>Tel</th>
                                <td><!-- Ici, vous peux ajouter le numéro de téléphone de l'utilisateur --></td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </section>`;

    const link = document.querySelector('#editButton');
    link.addEventListener('click', (e) => {
        e.preventDefault();
        Navigate('/modification');
    });
}

export default ProfilPage;

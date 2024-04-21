import { getAuthenticatedUser } from "../../utils/auths"
import { clearPage } from "../../utils/render";
import Navigate from "../Router/Navigate";

const creationSupervisorPage = async () => {
    if (!getAuthenticatedUser()) {
        Navigate('/login');
        return;
    };

    clearPage();
    await renderCreationSupervisorPage();
};

async function renderCreationSupervisorPage() {
    const main = document.querySelector('main');
    main.innerHTML = `
        <section>
            <div class="container h-100">
                <div class="row d-flex justify-content-center align-items-center h-100">
                    <div class="col-12 text-center">
                        <h1>Créer un responsable de stage</h1>
                    </div>
                </div>
            </div>
        </section>`;
}

export default creationSupervisorPage;
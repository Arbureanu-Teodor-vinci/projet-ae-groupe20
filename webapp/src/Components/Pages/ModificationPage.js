import { clearPage } from "../../utils/render"

const ModificationPage = async () => {
    clearPage();
    await renderModificationPage();
}

async function renderModificationPage() {
    const main = document.querySelector('main');
    main.innerHTML = `
    <section>
        <div class="container h-100">
            <div class="row d-flex justify-content-center align-items-center h-100">
                <div class="col-12 text-center">
                    <h1>Modifier mes données personnelles</h1>
                </div>
            </div>
        </div>
    </section>`;
};

export default ModificationPage;
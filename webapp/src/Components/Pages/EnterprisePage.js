import { clearPage } from "../../utils/render"

const EnterprisePage = async () => {
    clearPage();
    await renderEnterprisePage();
}

async function renderEnterprisePage() {
    const main = document.querySelector('main');
    main.innerHTML = `
    <section>
        <div class="container h-100">
            <div class="row d-flex justify-content-center align-items-center h-100">
                <div class="col-12 text-center">
                    <h1>Enterprise</h1>
                </div>
            </div>
        </div>
    </section>`;
};

export default EnterprisePage;
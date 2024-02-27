import { clearPage} from '../../utils/render';

const HomePage = () => {
    clearPage();
    renderHomePage();
};

function renderHomePage() {
    const main = document.querySelector('main');
  main.innerHTML = `<div class="container">
    <div class="row">
        <div class="col-md-12">
        <h1 class="text-primary text-decoration-underline mb-4 mt-3">Accueil</h1>
        <p>Bienvenue sur notre site web</p>
    </div>
  `;
}
export default HomePage;

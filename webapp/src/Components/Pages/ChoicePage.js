import { clearPage} from '../../utils/render';

const ChoicePage = () => {
    clearPage();
    renderChoicePage();
};

function renderChoicePage() {
    const main = document.querySelector('main');
    main.innerHTML = `<div class="container">
    <div class="row">
    <a href="/register" class="btn btn-primary btn-lg">@vinci.be</a> 
    <a href="/register" class="btn btn-primary btn-lg">@student.vinci.be</a> 
    </div>  
`;
}

export default ChoicePage;
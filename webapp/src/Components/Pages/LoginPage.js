import { clearPage } from '../../utils/render';
import Navigate from '../Router/Navigate';
import { setAuthenticatedUser } from '../../utils/auths';

const LoginPage = async () => {
    clearPage();
    await renderLoginPage();
    const form = document.querySelector('form');
    form.addEventListener('submit', login);
};

async function renderLoginPage() {
    const main = document.querySelector('main');
    main.innerHTML = `
    <section class="vh-100" ;">
    <div class="container h-100">
        <div class="row d-flex justify-content-center align-items-center h-100">
            <div class="col-xl-9">
    
                <h1 class="text-primary text-decoration-underline mb-4 mt-3">Connexion</h1>
    
                    <form class="card text-dark  border-primary bg-white mb-3" style="border-radius: 15px;">
                        <div class="card-body">
    
                            <div class="row align-items-center pt-4 pb-3">
                                <div class="col-md-3 ps-5">
    
                                    <h6 class="mb-0">Adresse e-mail</h6>
    
                                </div>
                                <div class="col-md-9 pe-5">
                                <input type="text" class="form-control form-control-lg" id="emailInput" required>
                                </div>
                            </div>
    
                            <hr class="mx-n3">
    
                            <div class="row align-items-center py-3">
                                <div class="col-md-3 ps-5">
    
                                    <h6 class="mb-0">Mot de passe</h6>
    
                                </div>
                                <div class="col-md-9 pe-5">
    
                                    <input type="password" id="pwd" class="form-control form-control-lg" required>
    
                                </div>
                            </div>
                            <p class = "noLogin errorMessage"><p>
    
                            <hr class="mx-n3">
                            <div class="mx-5">
                                <a href="" id = "toRegister">Pas encore inscrit ? Inscrivez-vous ici</a>  
                            </div>

                            <div class="px-5 py-4">
                                <button type="submit" class="btn btn-primary btn-lg" id="connect">Connexion</button>
                            </div>
                            <div class="px-5 py-4" id="error"></div>

                        </div>
                    </form>
    
                </div>
            </div>
        </div>

        
    </section>
    `;

    const link = document.querySelector('#toRegister');
    link.addEventListener('click', (e) => {
    e.preventDefault();
    Navigate('/register')

})
};


async function login (e) {
    e.preventDefault();

    const email = document.querySelector('#emailInput').value;
    const password = document.querySelector('#pwd').value;

    const options = {
      method: 'POST',
      body: JSON.stringify({
        email,
        password,
      }),
      headers : {
        'Content-Type': 'application/json',
      },
    };
    
    const response = await fetch(`/api/auths/login`, options);

    if(!response.ok) {
      const erreur = document.querySelector('.noLogin');
      erreur.innerText = "Nom d'utilisateur ou mot de passe incorrect";
    }else{
      const authenticatedUser = await response.json();
      setAuthenticatedUser(authenticatedUser.token);
      Navigate('/');
    };

    return null;
  
  };




export default LoginPage;

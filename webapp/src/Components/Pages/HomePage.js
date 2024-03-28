import {  getAuthenticatedUser } from '../../utils/auths'
import Navigate from '../Router/Navigate';
import Navbar from '../Navbar/Navbar';

const HomePage = () => {
  if(!getAuthenticatedUser()){
    Navbar();
    Navigate('/login');
  }else if (getAuthenticatedUser().role === 'Etudiant'){
    Navbar();
    Navigate('/profil');
  }else if (getAuthenticatedUser().role === 'Professeur' || getAuthenticatedUser().role === 'Administratif'){
    Navbar();
    Navigate('/board');
  }
};

export default HomePage;

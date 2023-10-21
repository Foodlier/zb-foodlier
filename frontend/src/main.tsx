import axios from 'axios'
import ReactDOM from 'react-dom/client'
import { BrowserRouter } from 'react-router-dom'
import { RecoilRoot } from 'recoil'
import App from './App'
import './reset.css'
// import { worker } from './mocks/browsers'

axios.defaults.withCredentials = true

// if (process.env.NODE_ENV === 'development') {
//   worker.start()
// }

ReactDOM.createRoot(document.getElementById('root')!).render(
  <BrowserRouter>
    <RecoilRoot>
      <App />
    </RecoilRoot>
  </BrowserRouter>
)

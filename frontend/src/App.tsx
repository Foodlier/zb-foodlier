import './index.css'
import { Routes, Route } from 'react-router-dom'
import Login from './pages/Login'

function App() {
  // const [count, setCount] = useState(0)
  // useEffect(() => {
  //   fetch('/api/test')
  //     .then(response => response.json())
  //     .then(json => setMessage(json.SUCCESS_TEXT))
  // }, [])

  return (
    <Routes>
      <Route path="/" element={<Login />} />
    </Routes>
  )
}

export default App

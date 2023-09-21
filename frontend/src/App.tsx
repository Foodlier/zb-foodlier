import styled from 'styled-components'
import logo from './assets/logo.svg'
import google from './assets/google.svg'
import kakao from './assets/kakao.svg'
import './index.css'

// 스타일드 컴포넌트
// TODO
const Container = styled.div`
  margin: 0 auto;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100vh;
`

const Title = styled.h1`
  font-size: 24px;
  margin-bottom: 50px;
`

const Form = styled.form`
  display: flex;
  flex-direction: column;
  width: 80%;
  max-width: 400px;
`

const Input = styled.input`
  height: 30px;
  padding: 10px;
  margin-bottom: 15px;
  border: 1px solid #ccc;
  border-radius: 12px;
  font-size: 16px;
`

const Button = styled.button`
  height: 55px;
  padding: 10px;
  margin-bottom: 10px;
  background-color: #e45141;
  color: #fff;
  border: none;
  border-radius: 12px;
  font-size: 16px;
  cursor: pointer;
`

// 구분선 컴포넌트
const Divider = styled.div`
  width: 90%;
  height: 1px;
  background-color: #ccc;
  margin: 10px 0;
`

const RegisterButton = styled(Button)`
  background-color: #fff;
  margin-top: 10px;
  color: #e45141;
  border: 1px solid #e45141;
`

const SocialLoginButton = styled(Button)`
  width: 80%;
  background-color: #fff;
  margin-top: 10px;
  color: #000;
  border: 1px solid #393939;
  display: flex;
  align-items: center;
  justify-content: center;
`

function App() {
  // const [count, setCount] = useState(0)

  // useEffect(() => {
  //   fetch('/api/test')
  //     .then(response => response.json())
  //     .then(json => setMessage(json.SUCCESS_TEXT))
  // }, [])

  return (
    <Container>
      <Title>
        <img src={logo} alt="React Logo" />
      </Title>
      <Form>
        <Input type="email" placeholder="Email" />
        <Input type="password" placeholder="Password" />
        <Button type="submit">로그인</Button>
        <RegisterButton>회원가입</RegisterButton>
      </Form>
      <Divider />
      <SocialLoginButton>
        <img src={google} alt="" />
        <span>Sign in With Google</span>
      </SocialLoginButton>
      <SocialLoginButton>
        <img src={kakao} alt="" />
        <span>Sign in With Kakao</span>
      </SocialLoginButton>
    </Container>
  )
}

export default App

// 스타일드 컴포넌트
// TODO
import styled from 'styled-components'

export const inputStyles = `
  height: 55px;
  padding: 10px 30px;
  margin-bottom: 15px;
  border: 1px solid #ccc;
  border-radius: 12px;
  font-size: 16px;
`

export const buttonStyles = `
  height: 55px;
  padding: 10px;
  margin-bottom: 10px;
  border: none;
  border-radius: 12px;
  font-size: 16px;
  cursor: pointer;
`

export const Container = styled.div`
  margin: 0 auto;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100vh;
`

export const Title = styled.div`
  width: 70%;
  font-size: 24px;
  margin-bottom: 50px;
  text-align: center;
`

export const Logo = styled.img`
  margin-bottom: 50px;
  margin: 0 auto;
`

export const Form = styled.form`
  display: flex;
  flex-direction: column;
  width: 80%;
  max-width: 400px;
`

export const Input = styled.input`
  ${inputStyles}
`

export const FindPassword = styled.div`
  display: flex;
  justify-content: flex-start;
  height: 30px;
  padding: 10px;
  margin-bottom: 15px;
  font-size: 14px;
  color: #000;
  cursor: pointer;
`

export const Button = styled.button`
  ${buttonStyles}
  background-color: #e45141;
  color: #fff;
`

// 구분선 컴포넌트
export const Divider = styled.div`
  width: 90%;
  height: 1px;
  background-color: #ccc;
  margin: 10px 0;
`

export const RegisterButton = styled(Button)`
  ${buttonStyles}
  background-color: #fff;
  margin-top: 10px;
  color: #e45141;
  border: 1px solid #e45141;
`

export const SocialLoginButton = styled(Button)`
  ${buttonStyles}
  width: 80%;
  background-color: #fff;
  margin-top: 10px;
  color: #000;
  border: 1px solid #393939;
  display: flex;
  align-items: center;
  justify-content: center;
`

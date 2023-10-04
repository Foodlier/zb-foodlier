// 스타일드 컴포넌트
// TODO :비밀번호 찾기
import styled from 'styled-components'

export const Container = styled.div`
  margin: 0 auto;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100vh;
`

export const Title = styled.h1`
  font-size: 24px;
  margin-bottom: 50px;
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
  margin-bottom: 15px;
  border: 1px solid #ccc;
  border-radius: 12px;
  font-size: 16px;
`

export const Button = styled.button`
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
export const Divider = styled.div`
  position: relative;
  width: 90%;
  height: 1px;
  background-color: #ccc;
  margin: 25px 0;
  text-align: center;
  z-index: 1;
  &:before {
    content: '';
    position: absolute;
    top: -5px;
    left: 50%;
    transform: translateX(-50%);
    width: 100px;
    height: 10px;
    background-color: #fff;
  }
`

export const Text = styled.span`
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  background-color: #fff;
  padding: 0 10px;
  font-size: 18px;
  color: #ccc;
  z-index: 2;
`

export const RegisterButton = styled(Button)`
  background-color: #fff;
  margin-top: 10px;
  color: #e45141;
  border: 1px solid #e45141;
`

export const SocialLoginButton = styled(Button)`
  ${buttonStyles}
  background-color: #fff;
  margin-top: 10px;
  color: #000;
  border: 1px solid #393939;
  display: flex;
  align-items: center;
  justify-content: center;
`

export const SocialLogo = styled.img`
  width: 20px;
  height: 20px;
  margin-right: 10px;
`

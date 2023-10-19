// 비밀번호 찾기 스타일 컴포넌트
import { useState } from 'react'
import styled from 'styled-components'
import { Link } from 'react-router-dom'
import axios from 'axios'
import axiosInstance from '../../utils/FetchCall'

const Container = styled.div`
  width: 400px;
  margin: 0 auto;
  padding: 100px 0;
  text-align: center;
`

const Title = styled.h1`
  font-size: 2rem;
  font-weight: 600;
  margin-bottom: 50px;
`

const Form = styled.form`
  display: flex;
  flex-direction: column;
`

const InputBox = styled.div`
  display: flex;
  flex-direction: column;
  margin-bottom: 20px;
`

const Label = styled.label`
  font-size: 1.2rem;
  margin-bottom: 30px;
`

const Input = styled.input`
  width: 100%;
  padding: 10px;
  font-size: 1.2rem;
  border: 1px solid #ccc;
  border-radius: 4px;
`

const Button = styled.button`
  width: 100%;
  padding: 10px;
  font-size: 1.2rem;
  border: none;
  background-color: #fff;
  cursor: pointer;
`

const FindPasswordPage = () => {
  const [userInputs] = useState({
    phoneNumber: '01099989331',
    email: 'bos3321@gmail.com',
  })

  const handleSubmitFindPassword = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault()
    axiosInstance
      .post('/api/auth/findPassword', userInputs)
      .then(res => {
        alert('임시 비밀번호가 이메일로 전송되었습니다.')
        console.log(res)
      })
      .catch(err => {
        if (err.response === 400) {
          alert('핸드폰 번호 또는 이메일이 일치하지 않습니다.')
          return
        }
        console.log(err)
      })
  }

  return (
    <Container>
      <Title>비밀번호 찾기</Title>
      <Form onSubmit={handleSubmitFindPassword}>
        <InputBox>
          <Label htmlFor="phoneNumber">핸드폰 번호</Label>
          <Input
            id="phoneNumber"
            type="phoneNumber"
            value={userInputs.phoneNumber}
          />
        </InputBox>
        <InputBox>
          <Label htmlFor="email">이메일</Label>
          <Input id="email" type="email" value={userInputs.email} />
          <Button type="submit">비밀번호 찾기</Button>
        </InputBox>
      </Form>
      <Link to="/login">로그인</Link>
    </Container>
  )
}

export default FindPasswordPage

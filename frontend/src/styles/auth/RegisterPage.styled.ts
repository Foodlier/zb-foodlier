import styled from 'styled-components'
// import React, { useState } from 'react'
// import { Modal } from '../../components/auth/AddressModal'
import { palette } from '../../constants/Styles'

// 회원가입 페이지 스타일 컴포넌트
const inputStyles = `
  height: 55px;
  padding: 10px 10px;
  margin-bottom: 15px;
  border: 1px solid #ccc;
  border-radius: 12px;
  font-size: 16px;
`

const buttonStyles = `
  height: 55px;
  padding: 10px;
  margin: 0 5px;
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

export const Profile = styled.div`
  width: 100px;
  height: 100px;
  margin: 0 auto;
  padding: 10px 10px;
  border: 1px solid #ccc;
  border-radius: 20%;
  margin-bottom: 5px;
  text-align: center;
`

export const ProfileImage = styled.img`
  width: 100%;
  height: 100%;
  margin: 0 auto;
  text-align: center;
`
export const ProfileButton = styled.button`
  ${buttonStyles}
  height: 40px;
  background-color: ${palette.main};
  color: #fff;
  font-size: 14px;
  margin: 0 auto 10px auto;
`

export const InputContainer = styled.div`
  width: 100%;
  align-items: center;
  display: flex;
  justify-content: space-between;
  flex-wrap: wrap;
  margin-bottom: 5px;
`
export const InputInfo = styled.span`
  display: flex;
  flex-direction: column;
  width: 35%;
  max-width: 400px;
  margin-bottom: 10px;
`

export const Title = styled.h1`
  font-size: 24px;
  margin-bottom: 10px;
`

export const Form = styled.form`
  display: flex;
  flex-direction: column;
  width: 90%;
  max-width: 500px;
`

export const FileInput = styled.input`
  display: none;
`

export const Input1 = styled.input`
  ${inputStyles}
  width: 100%;
`

export const Input2 = styled.input`
  ${inputStyles}
  width: 70%;
`

export const FindPassword = styled.div`
  display: flex;
  justify-content: flex-start;
  margin-bottom: 15px;
  font-size: 14px;
  color: #000;
  cursor: pointer;
`

export const Button = styled.button`
  ${buttonStyles}
`

export const DetailButton = styled.button`
  ${buttonStyles}
  width: 25%;
  border: 1px solid ${palette.main};
  color: ${palette.main};
  background-color: #fff;
  padding: 10px 10px;
  margin-bottom: 15px;
`

export const ConfirmButton = styled.button`
  ${buttonStyles}
  width: 80%;
  height: 65px;
  background-color: ${palette.main};
  color: #fff;
  font-size: 18px;
  margin: 0 auto;
`

export const DisabledButton = styled.button`
  ${buttonStyles}
  width: 80%;
  height: 65px;
  background-color: #ccc;
  color: #fff;
  font-size: 18px;
  margin: 0 auto;
`

export const ErrorMessage = styled.div`
  width: 100%;
  color: red;
  font-size: 14px;
  margin-left: 10px;
  margin-bottom: 5px;
`

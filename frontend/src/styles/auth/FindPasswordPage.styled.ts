/* eslint-disable import/prefer-default-export */
import styled from 'styled-components'
import { breakpoints, typography, palette } from '../../constants/Styles'

export const buttonStyles = `
  height: 55px;
  padding: 10px 30px;
  margin-bottom: 15px;
  border: 1px solid #ccc;
  border-radius: 1rem;
  font-weight: 400;
  @media ${breakpoints.small} {
    font-size: ${typography.mobile.content};
  }
  @media ${breakpoints.large} {
    font-size: ${typography.web.content};
  }
`

export const inputStyles = `
  height: 55px;
  padding: 10px 30px;
  margin-bottom: 15px;
  border: 1px solid #ccc;
  border-radius: 1rem;
  font-weight: 400;
  @media ${breakpoints.small} {
    font-size: ${typography.mobile.content};
  }
  @media ${breakpoints.large} {
    font-size: ${typography.web.content};
  }
`

export const Container = styled.div`
  width: 400px;
  margin: 0 auto;
  padding: 150px 0;
  text-align: center;
  height: 100vh;
`

export const Logo = styled.img`
  width: 20em;
  margin: 0px auto 20px auto;
`

export const Title = styled.h1`
  font-weight: 600;
  margin-bottom: 50px;
  ${breakpoints.small} {
    font-size: ${typography.mobile.mainTitle};
  }
  ${breakpoints.large} {
    font-size: ${typography.web.mainTitle};
  }
`

export const Form = styled.form`
  display: flex;
  flex-direction: column;
`

export const InputBox = styled.div`
  display: flex;
  flex-direction: column;
  margin-bottom: 20px;
`

export const Label = styled.label`
  margin-bottom: 20px;
  text-align: left;
  ${breakpoints.small} {
    font-size: ${typography.mobile.subTitle};
  }
  ${breakpoints.large} {
    font-size: ${typography.web.subTitle};
  }
`

export const Input = styled.input`
  ${inputStyles}
`

export const FindPasswordButton = styled.button`
  ${buttonStyles}
  background-color: ${palette.main};
  color: ${palette.white};
`

export const HomeButton = styled.button`
  ${buttonStyles}
  background-color: ${palette.white};
  color: ${palette.main};
`

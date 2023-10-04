import styled from 'styled-components'
import { palette } from '../../constants/Styles'

export const Container = styled.section`
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-right: 3rem;
`
export const Image = styled.img`
  width: 10rem;
  height: auto;
  max-width: 100%;
  max-height: 10rem;
  border-radius: 50%;
  border: 0.3rem solid ${palette.main};
  padding: 15%;
`
export const Nickname = styled.p`
  margin-top: 1rem;
  font-weight: 600;
`

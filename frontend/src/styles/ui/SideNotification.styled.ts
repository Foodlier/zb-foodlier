import styled from 'styled-components'
import { palette } from '../../constants/Styles'

export const SideBar = styled.div`
  background-color: ${palette.white};
  position: fixed;
  top: 0;
  bottom: 0;
  right: 0;
  transition: 0.4s ease;
  border-left: 1px solid ${palette.divider};
  height: 100%;
  z-index: 99;
`

export const Button = styled.button`
  position: absolute;
  left: -7rem;
  top: 2rem;
  padding: 1rem;
  z-index: 10000;
  transition: 0.8s ease;
  /* border: 1px solid ${palette.textSecondary}; */
  border-radius: 100%;
  overflow: hidden;
  background-color: white;
`

export const Content = styled.div`
  position: relative;
  width: 100%;
  height: 100vh;
  overflow-y: auto;
`

export const EmptyList = styled.div`
  display: flex;
  align-items: center;
  justify-content: center;
  color: ${palette.textSecondary};
  padding: 5rem;
`

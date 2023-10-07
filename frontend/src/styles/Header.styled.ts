import styled from 'styled-components'
import { breakpoints, palette, zindex } from '../constants/Styles'

export const Container = styled.header`
  position: sticky;
  top: 0;
  max-height: 9rem;
  height: 9rem;
  padding: 5%;
  display: flex;
  align-items: center;
  justify-content: space-between;
  background-color: ${palette.white};
  z-index: ${zindex.header};

  ${breakpoints.large} {
    padding: 2%;
  }
`

export const LogoButton = styled.button`
  width: 30%;
`

export const Logo = styled.img`
  width: 100%;
  height: auto;

  ${breakpoints.large} {
    width: 50%;
  }
`

export const WrapMenu = styled.ul`
  display: none;
  align-items: center;

  ${breakpoints.large} {
    display: flex;
  }
`

export const Menu = styled.button`
  padding: 0px 1.8rem;
  font-size: 1.8rem;
  font-weight: 600;
  color: ${palette.textPrimary};
`

export const Notification = styled.div<{ $isToggle: boolean }>`
  position: absolute;
  display: ${props => (props.$isToggle ? 'flex' : 'none')};
  flex-direction: column;
  min-width: 25rem;
  top: 5rem;
  border: 1px solid ${palette.divider};
  border-radius: 1rem;
  font-size: 1.4rem;
`

export const WrapIcon = styled.ul`
  display: flex;

  ${breakpoints.large} {
    display: none;
  }
`

export const Icon = styled.button`
  margin-left: 1rem;
`

export const LoginButton = styled.button`
  padding: 0.8rem 1.2rem;
  background-color: ${palette.main};
  color: white;
  border-radius: 1rem;
  margin-left: 1rem;
  font-size: 1.6rem;
  font-weight: 600;
`

export const Button = styled.button``

export const WrapNotification = styled.div`
  position: relative;
  display: flex;
  align-items: center;
  justify-content: flex-end;
  padding: 0px 1.8rem;
  font-size: 1.8rem;
  font-weight: 600;
  color: ${palette.textPrimary};
  z-index: ${zindex.header + 1};
`

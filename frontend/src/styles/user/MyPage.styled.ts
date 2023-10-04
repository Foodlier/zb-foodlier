import styled from 'styled-components'
import { breakpoints, palette } from '../../constants/Styles'

export const Container = styled.div`
  max-width: 80rem;
  margin: 0 auto;
  padding: 2rem 2rem 10rem;

  ${breakpoints.large} {
    padding: 2rem 2rem;
  }
`

export const ProfileButton = styled.button`
  width: 100%;
  display: flex;
  align-items: center;
`

export const ProfileImage = styled.img`
  width: 5rem;
  height: 5rem;
  border-radius: 2.5rem;
  background-color: gray;

  ${breakpoints.large} {
    width: 10rem;
    height: 10rem;
    border-radius: 5rem;
  }
`

export const PrifileInfo = styled.div`
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  margin-left: 1rem;
`
export const Nickname = styled.span`
  font-size: 1.6rem;
  font-weight: 800;
  margin-bottom: 0.2rem;
  color: ${palette.textPrimary};

  ${breakpoints.large} {
    font-size: 2.4rem;
    margin-bottom: 0.5rem;
  }
`
export const Email = styled.span`
  font-size: 1.4rem;
  color: ${palette.textSecondary};

  ${breakpoints.large} {
    font-size: 1.8rem;
  }
`

export const WrapMyPoint = styled.div`
  display: flex;
  align-items: center;
  justify-content: space-between;
  box-shadow: 1px 1px 1px ${palette.shadow};
  border: 1px solid ${palette.divider};
  border-radius: 1rem;
  padding: 1.5rem 3.5rem;
  margin: 3rem 0;
  font-size: 1.4rem;
  font-weight: 800;

  ${breakpoints.large} {
    padding: 3rem 7rem;
    font-size: 2rem;
  }
`

export const PointDetailButton = styled.button`
  display: flex;
  align-items: center;
`

export const PointChargeButton = styled.button`
  display: flex;
  align-items: center;
`

export const ChargeBadge = styled.div`
  background-color: ${palette.main};
  padding: 0.5rem 1rem;
  border-radius: 1.2rem;
  font-size: 1.2rem;
  color: ${palette.white};
  margin-right: 0.5rem;

  ${breakpoints.large} {
    font-size: 1.6rem;
    padding: 0.5rem 2rem;
    margin-right: 1rem;
  }
`

export const PointIcon = styled.div`
  display: flex;
  align-items: center;
  justify-content: center;
  width: 1.5rem;
  height: 1.5rem;
  background-color: #ffd542;
  color: ${palette.white};
  border-radius: 1rem;
  font-size: 1rem;
  margin-right: 0.5rem;

  ${breakpoints.large} {
    width: 2rem;
    height: 2rem;
    font-size: 1.2rem;
  }
`

export const NavigateButtonList = styled.div`
  display: flex;
  flex-direction: column;
  align-items: flex-start;
`

export const NavigateButton = styled.button<{ $isMargin: boolean }>`
  margin-bottom: ${props => (props.$isMargin ? 3 : 1.5)}rem;
  font-size: 1.4rem;

  ${breakpoints.large} {
    margin-bottom: ${props => (props.$isMargin ? 5 : 2)}rem;
    font-size: 1.8rem;
  }
`

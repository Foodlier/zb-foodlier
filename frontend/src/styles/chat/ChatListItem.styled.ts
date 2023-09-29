import styled from 'styled-components'
import { breakpoints, palette } from '../../constants/Styles'

export const WrapChatItem = styled.div`
  min-width: 15%;
  display: flex;
  align-items: center;
  justify-content: space-between;

  margin: 0 1rem;

  ${breakpoints.large} {
    padding: 2rem;
    border-bottom: 1px solid ${palette.divider};
    margin: 0;
  }
`

export const Wrap = styled.div`
  width: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;

  ${breakpoints.large} {
    width: auto;
    flex-direction: row;
  }
`

export const ProfileImage = styled.img`
  width: 4rem;
  height: 4rem;
  border-radius: 2rem;
  border: 1px solid black;

  ${breakpoints.large} {
    width: 5rem;
    height: 5rem;
    border-radius: 2.5rem;
  }
`

export const WrapProfileText = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;

  ${breakpoints.large} {
    margin-left: 1.5rem;
    align-items: flex-start;
  }
`

export const Nickname = styled.span`
  font-size: 1.2rem;
  color: ${palette.textPrimary};
  margin-top: 0.5rem;

  ${breakpoints.large} {
    font-size: 1.6rem;
    margin-top: 0;
  }
`

export const Price = styled.span`
  font-size: 1rem;
  color: ${palette.textSecondary};

  ${breakpoints.large} {
    font-size: 1.4rem;
  }
`
